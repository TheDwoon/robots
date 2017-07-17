package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.interaction.AiObserver;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.AI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sigma_000 on 17.07.2017.
 */
public class GameManager {

    private static final Logger log = LogManager.getLogger();

    public static ExecutorService oberverExecutor = Executors.newFixedThreadPool(4);

    private final BoardManager boardManager;

    private final InventoryManager inventoryManager;
    private final Map<AI, AiManager> aiManagers;

    private final Deque<AiObserver> observers;

    public GameManager(BoardManager boardManager) {
        this.boardManager = boardManager;
        this.inventoryManager = new InventoryManager();
        this.aiManagers = new HashMap<>();

        observers = new ConcurrentLinkedDeque<>();

        boardManager.addObserver(new BoardObserver() {
            @Override
            public void setSize(long uuid, int width, int height) {
                log.info("setSize(uuid = {}, width = {}, height = {})", uuid, width, height);
            }

            @Override
            public void updateFields(long uuid, Field[] fields) {
                log.info("updateFields(uuid = {}, ...)", uuid);
                for (Field field : fields) {
                    log.info("field: x = {}, y = {}, material = {}, occupied = {}, item = {}",
                            field.getX(), field.getY(), field.getMaterial(), field.isOccupied(), field.hasItem());
                }
            }
        });
    }

    public void addObserver(BoardObserver observer) {
        boardManager.addObserver(observer);
        log.info("BoardObserver");
    }

    public void removeObserver(BoardObserver observer) {
        boardManager.removeObserver(observer);
    }

    public void addObserver(InventoryObserver observer) {
        inventoryManager.addObserver(observer);
        log.info("InventoryObserver");
    }

    public void removeObserver(InventoryObserver observer) {
        inventoryManager.removeObserver(observer);
    }

    public void addObserver(AiObserver observer) {
        oberverExecutor.submit(() -> aiManagers.values().forEach(aiManager ->
                observer.spawnAi(aiManager.getControlledRobot(), aiManager.getControlledInventory())));
        observers.add(observer);
        log.info("AiObserver");
    }

    public void removeObserver(AiObserver observer) {
        observers.remove(observer);
    }

    private void notifyObserversSpawn(Robot robot, Inventory inventory) {
        oberverExecutor.submit(() -> observers.forEach(o -> o.spawnAi(robot, inventory)));
    }

    private void notifyObserversDespawn(Robot robot, Inventory inventory) {
        oberverExecutor.submit(() -> observers.forEach(o -> o.despawnAi(robot.getUUID(), inventory.getUUID())));
    }

    public synchronized AiManager spawnAi(AI ai) {
        Robot controlledRobot = new Robot();
        boardManager.spawnLivingEntity(controlledRobot);

        Inventory controlledInventory = new Inventory(12);
        inventoryManager.register(controlledRobot, controlledInventory);

        AiManager aiManager = new AiManager(ai, controlledRobot, controlledInventory, this);
        aiManagers.put(ai, aiManager);

        notifyObserversSpawn(controlledRobot, controlledInventory);
        return aiManager;
    }

    public synchronized void despawnAi(AI ai) {
        AiManager aiManager = aiManagers.remove(ai);
        if (aiManager == null) {
            return;
        }

        boardManager.removeLivingEntity(aiManager.getControlledRobot());
        inventoryManager.unregister(aiManager.getControlledRobot());

        notifyObserversDespawn(aiManager.getControlledRobot(), aiManager.getControlledInventory());
    }

    public void makeTurn() {
        for (AiManager aiManager : aiManagers.values()) {
            aiManager.makeTurn();
        }
    }

    public List<Field> getVisibleFields(LivingEntity entity) {
        return boardManager.getVisibleFields(entity.getX(), entity.getY());
    }

    public void robotForward(Robot robot) {
        boardManager.moveLivingEntityRelative(robot, robot.getFacing().dx, robot.getFacing().dy);
    }

    public void robotBackward(Robot robot) {
        boardManager.moveLivingEntityRelative(robot, robot.getFacing().dx, robot.getFacing().dy);
    }

    public void robotTurnLeft(Robot robot) {
        robot.setFacing(robot.getFacing().left());
    }

    public void robotTurnRight(Robot robot) {
        robot.setFacing(robot.getFacing().right());
    }

    public void robotUseItem(Robot robot, Item item) {
        if (inventoryManager.hasItem(robot, item)) {
            // TODO (sigmarw, 17.07.2017): define how this is applied
        }
    }

    public void robotPickUpItem(Robot controlledRobot) {
    }
}
