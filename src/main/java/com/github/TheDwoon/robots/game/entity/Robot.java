package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Facing;
import com.github.TheDwoon.robots.game.InventoryHolder;

public class Robot extends LivingEntity implements InventoryHolder {

	private static final int DEFAULT_MAX_HEALTH = 3;

	public Robot() {
		this(-1, -1);
	}

	public Robot(int x, int y) {
		super(x, y, DEFAULT_MAX_HEALTH);
	}

	public Robot(long uuid, int x, int y, int maxHealth, int health, Facing facing) {
		super(uuid, x, y, maxHealth, health, facing);
	}
}
