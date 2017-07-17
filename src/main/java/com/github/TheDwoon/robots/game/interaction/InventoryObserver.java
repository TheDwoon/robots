package com.github.TheDwoon.robots.game.interaction;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.items.Item;

public interface InventoryObserver {
	void createInventory(long inventoryHolderUuid, Inventory inventory);
	void deleteInventory(long uuid);
	void updateInventory(long uuid, int slot, Item item);	
}
