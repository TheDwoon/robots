package com.github.TheDwoon.robots.game.items;

import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.server.managers.BoardManager;
import com.github.TheDwoon.robots.server.managers.InventoryManager;

public class Star extends Item {

	public Star() {
		super();
	}
	
	public Star(int x, int y) {
		super(x, y);
	}
	
	@Override
	public void use(Robot robot, BoardManager boardManager, InventoryManager inventoryManager) {
		// do nothing
	}

}
