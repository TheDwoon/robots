package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.InventoryHolder;
import com.github.TheDwoon.robots.game.items.weapons.Weapon;

public class Robot extends LivingEntity implements InventoryHolder {
	private static final int MAX_HEALTH = 3;
	
	private final Inventory inventory;
	private Weapon weapon;

	public Robot(int x, int y, final Inventory inventory, final Weapon weapon) {
		super(x, y, MAX_HEALTH);
		
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

	void lookDown() {
		// TODO: implement network
	}
	
	void lookLeft() {
		// TODO: implement network
	}
	
	void lookRight() {
		// TODO: implement network
	}
}
