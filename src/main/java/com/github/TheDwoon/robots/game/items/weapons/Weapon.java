package com.github.TheDwoon.robots.game.items.weapons;

import com.github.TheDwoon.robots.game.items.Item;

public abstract class Weapon extends Item {

	public Weapon(final long uuid) {
		super(uuid);
	}

	public abstract void shoot();

	public abstract int getRange();

	public abstract boolean isPiercing();
}
