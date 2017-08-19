package com.github.TheDwoon.robots.server.managers;

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

	private final GameManager gameManager;

	public AiManager(AI ai, Robot controlledRobot, Inventory controlledInventory,
			GameManager gameManager) {
		this.ai = ai;
		this.controlledRobot = controlledRobot;
		this.controlledInventory = controlledInventory;

		this.gameManager = gameManager;
	}

	public void makeTurn() {
		try {
			ai.updateRobot(controlledRobot);
			ai.updateInventory(controlledInventory);
			ai.updateVision(gameManager.getVisibleFields(controlledRobot));
			PlayerAction action = ai.makeTurn();
			action.apply(this);
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
		gameManager.robotForward(controlledRobot);
	}

	public void driveBackward() {
		gameManager.robotBackward(controlledRobot);
	}

	public void turnLeft() {
		gameManager.robotTurnLeft(controlledRobot);
	}

	public void turnRight() {
		gameManager.robotTurnRight(controlledRobot);
	}

	public void useItem(int slot) {
		gameManager.robotUseItem(controlledRobot, slot);
	}

	public void pickUpItem() {
		gameManager.robotPickUpItem(controlledRobot);
	}

	public void dropItem(int slot) {
		gameManager.robotDropItem(controlledRobot, slot);
	}
}
