package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.Facing;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.AI;
import com.github.TheDwoon.robots.server.actions.PlayerAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sigma_000 on 17.07.2017.
 */
public class AiManager {

	public static final Logger log = LogManager.getLogger();

	private final AI ai;
	private final Robot controlledRobot;
	private final Inventory controlledInventory;

	private final BoardManager boardManager;
	private final InventoryManager inventoryManager;

	public AiManager(AI ai, Robot controlledRobot, Inventory controlledInventory,
			BoardManager boardManager, InventoryManager inventoryManager) {
		this.ai = ai;
		this.controlledRobot = controlledRobot;
		this.controlledInventory = controlledInventory;

		this.boardManager = boardManager;
		this.inventoryManager = inventoryManager;
	}

	public void makeTurn() {
		try {
			ai.updateRobot(controlledRobot);
			ai.updateInventory(controlledInventory);
			ai.updateVision(
					boardManager.getVisibleFields(controlledRobot.getX(), controlledRobot.getY()));
			if (controlledRobot.isAlive()) {
				PlayerAction action = ai.makeTurn();
				action.apply(this);
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
				item -> item.use(controlledRobot, boardManager, inventoryManager));
	}

	public void pickUpItem() {
		if (inventoryManager.getFreeSlots(controlledRobot) <= 0) {
			return;
		}

		Item item = boardManager.removeItem(controlledRobot.getX(), controlledRobot.getY());
		if (item != null) {
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
