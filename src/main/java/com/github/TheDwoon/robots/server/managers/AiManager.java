package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.game.items.Inventory;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.game.interaction.AI;
import com.github.TheDwoon.robots.server.ScoreCallback;
import com.github.TheDwoon.robots.server.actions.PlayerAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sigma_000 on 17.07.2017.
 */
public class AiManager {

	private static final Logger log = LogManager.getLogger();

	private static final int RESPAWN_ROUNDS = 10;

	private final AI ai;
	private final Robot controlledRobot;
	private final Inventory controlledInventory;

	private int respawnCounter;

	private final BoardManager boardManager;
	private final InventoryManager inventoryManager;
	private final ScoreCallback scoreCallback;
	private final boolean doRespawn;

	public AiManager(AI ai, Robot controlledRobot, Inventory controlledInventory,
			BoardManager boardManager, InventoryManager inventoryManager,
			ScoreCallback scoreCallback, boolean doRespawn) {
		this.ai = ai;
		this.controlledRobot = controlledRobot;
		this.controlledInventory = controlledInventory;

		this.respawnCounter = -1;

		this.boardManager = boardManager;
		this.inventoryManager = inventoryManager;
		this.scoreCallback = scoreCallback;
		this.doRespawn = doRespawn;
	}

	public void init() {
		try {
			ai.updateRobot(controlledRobot);
			ai.updateInventory(controlledInventory);
			ai.updateVision(
					boardManager.getVisibleFields(controlledRobot.getX(), controlledRobot.getY()));
			ai.init();
		} catch (Throwable t) {
			log.catching(t);
		}
	}

	public void makeTurn() {
		try {
			ai.updateRobot(controlledRobot);
			ai.updateInventory(controlledInventory);
			ai.updateVision(
					boardManager.getVisibleFields(controlledRobot.getX(), controlledRobot.getY()));
			if (controlledRobot.isAlive()) {
				PlayerAction action = ai.makeTurn();
				if (action != null) {
					action.apply(this);
				}
			} else {
				if (respawnCounter < 0) {
					// just got killed
					respawnCounter = RESPAWN_ROUNDS;
				} else if (respawnCounter > 0) {
					// in respawn waiting phase
					respawnCounter--;
				} else if (doRespawn) {
					// respawn (if not possible, retry next round)
					if (boardManager.spawnLivingEntity(controlledRobot)) {
						log.info("Robot #{} has been respawned at ({}/{}).",
								controlledRobot.getUUID(), controlledRobot.getX(),
								controlledRobot.getY());
					} else {
						log.warn("Robot #{} has been added to the spawn queue.",
								controlledRobot.getUUID());
					}
					respawnCounter = -1;
				}
			}
		} catch (Throwable t) {
			log.catching(t);
		}
	}

	public Robot getControlledRobot() {
		return controlledRobot;
	}

	public Inventory getControlledInventory() {
		return controlledInventory;
	}

	public void increaseScore(int score) {
		controlledRobot.increaseScore(score);
	}

	public void driveForward() {
		Facing facing = controlledRobot.getFacing();
		boardManager.moveLivingEntityRelative(controlledRobot, facing.dx, facing.dy);
	}

	public void driveBackward() {
		Facing facing = controlledRobot.getFacing().opposite();
		boardManager.moveLivingEntityRelative(controlledRobot, facing.dx, facing.dy);
	}

	public void turnLeft() {
		boardManager.turnLivingEntity(controlledRobot, controlledRobot.getFacing().left());
	}

	public void turnRight() {
		boardManager.turnLivingEntity(controlledRobot, controlledRobot.getFacing().right());
	}

	public void useItem(int slot) {
		inventoryManager.useItem(controlledRobot, slot,
				item -> item.use(controlledRobot, boardManager, inventoryManager, scoreCallback));
	}

	public void pickUpItem() {
		if (inventoryManager.getFreeSlots(controlledRobot) <= 0) {
			return;
		}

		Item item = boardManager.getField(controlledRobot.getX(), controlledRobot.getY()).getItem();
		if (item != null && item.isCarriable()) {
			boardManager.removeItem(item);
			inventoryManager.giveItem(controlledRobot, item);
		}
	}

	public void dropItem(int slot) {
		if (boardManager.getField(controlledRobot.getX(), controlledRobot.getY()).hasItem()) {
			return;
		}

		Item item = inventoryManager.removeItem(controlledRobot, slot);
		if (item != null) {
			boardManager.spawnItem(item, controlledRobot.getX(), controlledRobot.getY());
		}
	}
}
