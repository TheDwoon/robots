package com.github.TheDwoon.robots.game;

import com.github.TheDwoon.robots.game.items.weapons.Weapon;

public class Robot {
	private final Inventory inventory;
	private Weapon weapon;

	public Robot(final Inventory inventory, final Weapon weapon) {
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
