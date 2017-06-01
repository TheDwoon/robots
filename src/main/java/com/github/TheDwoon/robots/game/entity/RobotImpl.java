package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Facing;
import com.github.TheDwoon.robots.game.Inventory;

public class RobotImpl extends LivingEntityImpl implements Robot {

    private static final int DEFAULT_MAX_HEALTH = 3;

    private final Inventory inventory;

    public RobotImpl(int x, int y, final Inventory inventory) {
        super(x, y, DEFAULT_MAX_HEALTH);

        this.inventory = inventory;
    }

    public RobotImpl(long uuid, int x, int y, int maxHealth, int health, Facing facing, Inventory inventory) {
		super(uuid, x, y, maxHealth, health, facing);
		
		this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
