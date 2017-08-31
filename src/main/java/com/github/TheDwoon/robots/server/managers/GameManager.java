package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.interaction.AI;
import com.github.TheDwoon.robots.game.interaction.AiObserver;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.game.items.Inventory;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.ScoreCallback;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sigma_000 on 17.07.2017.
 */
public class GameManager {

	public static ExecutorService observerExecutor = Executors.newFixedThreadPool(4);

	protected final BoardManager boardManager;

	protected final InventoryManager inventoryManager;
	protected final Map<AI, AiManager> aiManagers;
	protected final Map<Robot, AiManager> robots;

	private final Deque<AiObserver> observers;

	private final ScoreCallback scoreCallback;
	
	private boolean allowRespawn = true;

	public GameManager(BoardManager boardManager, boolean allowRespawn) {
		this(boardManager);
		
		this.allowRespawn = allowRespawn;
	}
	
	public GameManager(BoardManager boardManager) {
		scoreCallback = this::increaseScore;

		boardManager.setScoreCallback(scoreCallback);
		this.boardManager = boardManager;
		this.inventoryManager = new InventoryManager(scoreCallback);
		this.aiManagers = new HashMap<>();
		this.robots = new HashMap<>();

		observers = new ConcurrentLinkedDeque<>();
	}

	public void addObserver(BoardObserver observer) {
		boardManager.addObserver(observer);
	}

	public void removeObserver(BoardObserver observer) {
		boardManager.removeObserver(observer);
	}

	public void addObserver(InventoryObserver observer) {
		inventoryManager.addObserver(observer);
	}

	public void removeObserver(InventoryObserver observer) {
		inventoryManager.removeObserver(observer);
	}

	public void addObserver(AiObserver observer) {
		observerExecutor.submit(() -> {
			synchronized (aiManagers) {
				aiManagers.values().forEach(aiManager -> observer
						.spawnAi(aiManager.getControlledRobot(),
								aiManager.getControlledInventory()));
			}
		});
		observers.add(observer);
	}

	public void removeObserver(AiObserver observer) {
		observers.remove(observer);
	}

	private void notifyObserversSpawn(Robot robot, Inventory inventory) {
		observers.forEach(o -> observerExecutor.submit(() -> o.spawnAi(robot, inventory)));
	}

	private void notifyObserversDespawn(Robot robot, Inventory inventory) {
		observers.forEach(o -> observerExecutor
				.submit(() -> o.despawnAi(robot.getUUID(), inventory.getUUID())));
	}

	private void notifyObserversScoreUpdate(Robot robot) {
		observers.forEach(o -> observerExecutor
				.submit(() -> o.updateScore(robot.getUUID(), robot.getScore())));
	}

	private void notifyObserversRobotDead(Robot robot) {
		observers.forEach(o -> observerExecutor
				.submit(() -> o.robotDead(robot.getUUID())));
	}

	public synchronized AiManager spawnAi(AI ai) {
		Robot controlledRobot = new Robot(ai.getRobotName());
		boardManager.spawnLivingEntity(controlledRobot);

		Inventory controlledInventory = new Inventory(12);
		inventoryManager.register(controlledRobot, controlledInventory);

		AiManager aiManager = new AiManager(ai, controlledRobot, controlledInventory, boardManager,
				inventoryManager, scoreCallback, allowRespawn);
		aiManager.init();
		synchronized (aiManagers) {
			aiManagers.put(ai, aiManager);
		}
		synchronized (robots) {
			robots.put(controlledRobot, aiManager);
		}

		notifyObserversSpawn(controlledRobot, controlledInventory);
		return aiManager;
	}

	public synchronized void despawnAi(AI ai) {
		AiManager aiManager;
		synchronized (aiManagers) {
			aiManager = aiManagers.remove(ai);
		}
		if (aiManager == null) {
			return;
		}

		Robot robot = aiManager.getControlledRobot();
		synchronized (robots) {
			robots.remove(robot);
		}

		boardManager.removeLivingEntity(robot);
		inventoryManager.unregister(robot);

		notifyObserversDespawn(robot, aiManager.getControlledInventory());
	}

	public synchronized void spawnItems(Item... items) {
		for (Item item : items) {
			boardManager.spawnItem(item);
		}
	}

	public synchronized void spawnItemsPlaced(Item... items) {
		for (Item item : items) {
			boardManager.spawnItem(item, item.getX(), item.getY());
		}
	}

	public synchronized void spawnItem(Class<? extends Item> clazz, int x, int y)
			throws NoSuchMethodException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Constructor<? extends Item> constructor = clazz.getConstructor();
		Item item = constructor.newInstance();
		boardManager.spawnItem(item, x, y);
	}

	public synchronized void spawnItems(Class<? extends Item> clazz, int count)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
			InstantiationException {
		Constructor<? extends Item> constructor = clazz.getConstructor();
		for (int i = 0; i < count; i++) {
			boardManager.spawnItem(constructor.newInstance());
		}
	}

	public void increaseScore(Robot receiver, int score) {
		AiManager aiManager;
		synchronized (robots) {
			aiManager = robots.get(receiver);
		}
		if (aiManager != null) {
			aiManager.increaseScore(score);
		}
		notifyObserversScoreUpdate(receiver);
	}

	public void makeTurn() {
		synchronized (aiManagers) {
			aiManagers.values().forEach(AiManager::makeTurn);
			// TODO (sigmarw, 22.08.2017): check win condition
		}
		synchronized (robots) {
			robots.keySet().forEach(robot -> {
				if (robot.isDead()) {
					notifyObserversRobotDead(robot);
				}
			});
		}
	}
}
