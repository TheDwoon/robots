package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Item;

/**
 * Created by sigma_000 on 17.07.2017.
 */
public class GameManager {

    private final Board board;

    private final InventoryManager inventoryManager;

    public GameManager(Board board) {
        this.board = board;
        this.inventoryManager = new InventoryManager();
    }

    public void robotForward(Robot robot) {
        board.moveLivingEntityRelative(robot, robot.getFacing().dx, robot.getFacing().dy);
    }

    public void robotBackward(Robot robot) {
        board.moveLivingEntityRelative(robot, robot.getFacing().dx, robot.getFacing().dy);
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
