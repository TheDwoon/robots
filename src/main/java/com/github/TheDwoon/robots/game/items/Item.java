package com.github.TheDwoon.robots.game.items;

import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.server.ScoreCallback;
import com.github.TheDwoon.robots.server.managers.BoardManager;
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

	public abstract void use(Robot robot, BoardManager boardManager,
			InventoryManager inventoryManager, ScoreCallback scoreCallback);

	public boolean isReusable() {
		return false;
	}

	public boolean isCarriable() {
		return true;
	}
}
