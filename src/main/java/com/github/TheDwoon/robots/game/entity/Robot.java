package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.InventoryHolder;
import com.github.TheDwoon.robots.game.items.weapons.Weapon;

import java.nio.file.Watchable;

public class Robot extends LivingEntity implements InventoryHolder {

    private static final int MAX_HEALTH = 3;

    private final Inventory inventory;
    private Weapon weapon;

    public Robot(int x, int y, final Inventory inventory, final Weapon weapon) {
        super(x, y, MAX_HEALTH);

        this.inventory = inventory;
        this.weapon = weapon;
    }

    public Robot(long uuid, int x, int y, int maxHealth, int health, Inventory inventory,
            Weapon weapon) {
		super(uuid, x, y, maxHealth, health);
		this.inventory = inventory;
		this.weapon = weapon;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
