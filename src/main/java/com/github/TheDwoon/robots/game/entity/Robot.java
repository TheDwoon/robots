package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Facing;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.InventoryHolder;

public class Robot extends LivingEntity implements InventoryHolder {

    private static final int DEFAULT_MAX_HEALTH = 3;

    private final Inventory inventory;

    public Robot(int x, int y, final Inventory inventory) {
        super(x, y, DEFAULT_MAX_HEALTH);

        this.inventory = inventory;
    }

    public Robot(long uuid, int x, int y, int maxHealth, int health, Facing facing, Inventory inventory) {
		super(uuid, x, y, maxHealth, health, facing);
		
		this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
