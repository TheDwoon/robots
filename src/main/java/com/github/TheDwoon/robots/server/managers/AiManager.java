package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.AI;
import com.github.TheDwoon.robots.server.actions.PlayerAction;

/**
 * Created by sigma_000 on 17.07.2017.
 */
public class AiManager {

    private final AI ai;
    private final Robot controlledRobot;
    private final Inventory controlledInventory;

    private final GameManager gameManager;

    public AiManager(AI ai, Robot controlledRobot, Inventory controlledInventory, GameManager gameManager) {
        this.ai = ai;
        this.controlledRobot = controlledRobot;
        this.controlledInventory = controlledInventory;

        this.gameManager = gameManager;
    }

    public void makeTurn() {
        ai.updateRobot(controlledRobot);
        ai.updateInventory(controlledInventory);
        ai.updateVision(gameManager.getVisibleFields(controlledRobot));
        PlayerAction action = ai.makeTurn();
        action.apply(this);
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

    public void useItem(Item item) {
        gameManager.robotUseItem(controlledRobot, item);
    }

    public void pickUpItem() {
        gameManager.robotPickUpItem(controlledRobot);
    }
}
