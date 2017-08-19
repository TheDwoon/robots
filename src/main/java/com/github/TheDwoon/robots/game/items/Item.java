package com.github.TheDwoon.robots.game.items;

import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.server.managers.BoardManager;
import com.github.TheDwoon.robots.server.managers.GameManager;
import com.github.TheDwoon.robots.server.managers.InventoryManager;

public abstract class Item extends Entity {
	public Item() {
		super(-1, -1);
	}

	public Item(int x, int y) {
		super(x, y);
	}

	public Item(long uuid, int x, int y) {
		super(uuid, x, y);
	}
	
	public abstract void use(Robot robot, GameManager gameManager, BoardManager boardManager, InventoryManager inventoryManager);

	public boolean isReusable() {
		return false;
	}

}
