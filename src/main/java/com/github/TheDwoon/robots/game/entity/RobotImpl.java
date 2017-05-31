package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Inventory;

public class RobotImpl extends LivingEntityImpl implements Robot {

    private static final int MAX_HEALTH = 3;

    private final Inventory inventory;

    public RobotImpl(int x, int y, final Inventory inventory) {
        super(x, y, MAX_HEALTH);

        this.inventory = inventory;
    }

    public RobotImpl(long uuid, int x, int y, int maxHealth, int health, Inventory inventory) {
		super(uuid, x, y, maxHealth, health);
		
		this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
