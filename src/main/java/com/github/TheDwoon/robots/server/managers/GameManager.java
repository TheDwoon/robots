package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sigma_000 on 17.07.2017.
 */
public class GameManager {

    private final Board board;

    private final InventoryManager inventoryManager;
    private final Map<AI, AiManager> aiManagers;

    public GameManager(Board board) {
        this.board = board;
        this.inventoryManager = new InventoryManager();
        this.aiManagers = new HashMap<>();
    }

    public synchronized AiManager spawnAi(AI ai) {
        Robot controlledRobot = new Robot();
        board.spawnLivingEntity(controlledRobot);

        Inventory controlledInventory = new Inventory(12);
        inventoryManager.register(controlledRobot, controlledInventory);

        AiManager aiManager = new AiManager(ai, controlledRobot, controlledInventory, this);
        aiManagers.put(ai, aiManager);
        return aiManager;
    }

    public synchronized void despawnAi(AI ai) {
        AiManager aiManager = aiManagers.remove(ai);
        if (aiManager == null) {
            return;
        }

        board.removeLivingEntity(aiManager.getControlledRobot());
        inventoryManager.unregister(aiManager.getControlledRobot());
    }

    public void makeTurn() {
        for (AiManager aiManager: aiManagers.values()) {
            aiManager.makeTurn();
        }
    }

    public List<Field> getVisibleFields(LivingEntity entity) {
        return board.getVisibleFields(entity.getX(), entity.getY());
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
