package com.github.TheDwoon.robots.game.items;

import com.github.TheDwoon.robots.game.InventoryHolder;
import com.github.TheDwoon.robots.game.entity.EntityImpl;

public abstract class ItemImpl extends EntityImpl implements Item {
	public ItemImpl() {
		super(0, 0);
	}
	
	public ItemImpl(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void use() {
		// TODO (danielw, 31.05.2017): implement
	}
	
	@Override
	public void pickUp(InventoryHolder entity) {
		// TODO (danielw, 27.04.16): implement
	}
	
	@Override
	public void drop() {
		// TODO (danielw, 27.04.16): implement
	}

}
