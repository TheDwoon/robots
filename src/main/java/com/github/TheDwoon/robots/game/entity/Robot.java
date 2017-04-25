package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.items.weapons.Weapon;

public class Robot extends Entity {
	private final Inventory inventory;
	private Weapon weapon;

	public Robot(final Field position, final Inventory inventory, final Weapon weapon) {
		super(position);
		this.inventory = inventory;
		this.weapon = weapon;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void switchWeapon(final Weapon weapon) {

	}

	void turnLeft() {
		// TODO: implement network
	}

	void turnRight() {
		// TODO: implement network
	}

	void moveForward() {
		// TODO: implement network
	}

	void moveBackward() {
		// TODO: implement network
	}

}
