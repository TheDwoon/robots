package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.InventoryHolder;
import com.github.TheDwoon.robots.game.items.weapons.Weapon;

/**
 * Created by sigma_000 on 30.05.2017.
 */
public interface Robot extends LivingEntity, InventoryHolder {
    @Override
    Inventory getInventory();

    Weapon getWeapon();
}
