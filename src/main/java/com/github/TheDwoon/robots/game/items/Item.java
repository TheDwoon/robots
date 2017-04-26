package com.github.TheDwoon.robots.game.items;

import com.github.TheDwoon.robots.game.InventoryHolder;
import com.github.TheDwoon.robots.game.entity.Entity;

public abstract class Item extends Entity {
	public Item() {
		super(0, 0);
	}
	
	public Item(int x, int y) {
		super(x, y);
	}
	
	public void pickUp(InventoryHolder entity) {
		// TODO (danielw, 27.04.16): implement
	}
	
	public void drop() {
		// TODO (danielw, 27.04.16): implement
	}
	
	public String getType() {
		return getClass().getSimpleName();
	}

}
