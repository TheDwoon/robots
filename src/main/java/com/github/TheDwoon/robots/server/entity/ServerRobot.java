package com.github.TheDwoon.robots.server.entity;

import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.entity.RobotImpl;
import com.github.TheDwoon.robots.game.items.weapons.Weapon;

/**
 * Created by sigma_000 on 30.05.2017.
 */
public class ServerRobot implements Robot {
    private final Robot robot;

    public ServerRobot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public int getMaxHealth() {
        return robot.getMaxHealth();
    }

    @Override
    public int getHealth() {
        return robot.getHealth();
    }

    @Override
    public void setHealth(int health) {
        robot.setHealth(health);
    }

    @Override
    public void damage(int damage) {
    robot.damage(damage);
    }

    @Override
    public boolean isAlive() {
        return robot.isAlive();
    }

    @Override
    public boolean isDead() {
        return robot.isDead();
    }

    @Override
    public long getUUID() {
        return robot.getUUID();
    }

    @Override
    public int getX() {
        return robot.getX();
    }

    @Override
    public void setX(int x) {
        robot.setX(x);
    }

    @Override
    public int getY() {
        return robot.getY();
    }

    @Override
    public void setY(int y) {
        robot.setY(y);
    }

    @Override
    public void setPosition(int x, int y) {
        robot.setPosition(x, y);
    }

    @Override
    public Board getBoard() {
        return robot.getBoard();
    }

    @Override
    public void setBoard(Board board) {
        robot.setBoard(board);
    }

    @Override
    public void update() {
        robot.update();
    }

    @Override
    public String getType() {
        return robot.getType();
    }

    @Override
    public Inventory getInventory() {
        return robot.getInventory();
    }

    @Override
    public Weapon getWeapon() {
        return robot.getWeapon();
    }

    public Robot getRobot() {
        return robot;
    }
}
