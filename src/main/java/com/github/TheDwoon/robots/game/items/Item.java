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

	public Item(long uuid, int x, int y) {
		super(uuid, x, y);
	}
	
	public void use() {
		// TODO (danielw, 31.05.2017): implement
	}
	
	public void drop() {
		// TODO (danielw, 27.04.16): implement
	}

}
