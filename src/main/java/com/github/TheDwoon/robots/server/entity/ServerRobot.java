package com.github.TheDwoon.robots.server.entity;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.weapons.Weapon;

/**
 * Created by sigma_000 on 30.05.2017.
 */
public class ServerRobot extends ServerLivingEntity implements Robot {
    private final Robot robot;

    public ServerRobot(Robot robot) {
    	super(robot);
    	
        this.robot = robot;
    }

    @Override
    public Inventory getInventory() {
        return robot.getInventory();
    }

    @Override
    public Weapon getWeapon() {
        return robot.getWeapon();
    }

    @Override
    public boolean equals(Object obj) {
        return robot.equals(obj);
    }

    @Override
    public int hashCode() {
        return robot.hashCode();
    }

    public Robot getRobot() {
        return robot;
    }
}
