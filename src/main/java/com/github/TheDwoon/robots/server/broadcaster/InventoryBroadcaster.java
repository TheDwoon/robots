package com.github.TheDwoon.robots.server.broadcaster;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.game.items.Item;

public final class InventoryBroadcaster extends Broadcaster<InventoryObserver> implements InventoryObserver {

	public InventoryBroadcaster() {
		super();
	}
	
	@Override
	public void createInventory(Inventory inventory) {
		notifyObservers(o -> o.createInventory(inventory));
	}

	@Override
	public void deleteInventory(long uuid) {
		notifyObservers(o -> o.deleteInventory(uuid));
	}

	@Override
	public void updateInventory(long uuid, int slot, Item item) {
		notifyObservers(o -> o.updateInventory(uuid, slot, item));
	}
}
