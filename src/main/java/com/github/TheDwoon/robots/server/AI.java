package com.github.TheDwoon.robots.server;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.server.actions.PlayerAction;

import java.util.List;

public interface AI {
    void updateRobot(Robot robot);

    void updateInventory(Inventory inventory);

    void updateVision(List<Field> fields);


    PlayerAction makeTurn();
}
