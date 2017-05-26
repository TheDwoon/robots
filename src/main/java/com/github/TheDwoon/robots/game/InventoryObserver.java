package com.github.TheDwoon.robots.game;

import com.github.TheDwoon.robots.game.items.Item;

public interface InventoryObserver {
	void createInventory(Inventory inventory);
	void deleteInventory(long uuid);
	void updateInventory(long uuid, int slot, Item item);	
}
