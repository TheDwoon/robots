package com.github.TheDwoon.robots.server;

import com.github.TheDwoon.robots.game.Inventory;

public interface Robot {
	void turnLeft();
	void turnRight();
	void driveForward();
	void driveBackwards();
	Inventory getInventory();
}
