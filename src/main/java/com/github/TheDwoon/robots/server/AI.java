package com.github.TheDwoon.robots.server;

import java.util.List;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.actions.PlayerAction;

public interface AI {
	// Methods to keep track of the robots state.
	void updateRobot(Robot entity);
	
	// Method to keep track of the inventory
	void updateInventory(long uuid, int slot, Item item);
	
	// Keeping track of the vision things.
	void updateVision(List<Field> fields, List<Entity> entities);
	

	PlayerAction makeTurn();
}
