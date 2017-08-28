package com.github.TheDwoon.robots.game.items;

import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.server.ScoreCallback;
import com.github.TheDwoon.robots.server.managers.BoardManager;
import com.github.TheDwoon.robots.server.managers.InventoryManager;

public class Beacon extends Item {

	public Beacon() {

	}

	public Beacon(long uuid, int x, int y) {
		super(uuid, x, y);
	}

	@Override
	public void use(Robot robot, BoardManager boardManager, InventoryManager inventoryManager,
			ScoreCallback scoreCallback) {
	}

	@Override
	public boolean isCarriable() {
		return false;
	}
}
