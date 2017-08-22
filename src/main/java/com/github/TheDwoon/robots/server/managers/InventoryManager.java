package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.items.Inventory;
import com.github.TheDwoon.robots.game.items.InventoryHolder;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.game.items.Item;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Consumer;

/**
 * Created by sigma_000 on 17.07.2017.
 */
public class InventoryManager {

	private final Map<InventoryHolder, Inventory> inventories;

	private final Deque<InventoryObserver> observers;

	public InventoryManager() {
		inventories = new HashMap<>();
		observers = new ConcurrentLinkedDeque<>();
	}

	public void addObserver(InventoryObserver observer) {
		GameManager.observerExecutor.submit(() -> inventories.forEach(
				(inventoryHolder, inventory) -> observer
						.createInventory(inventoryHolder.getUUID(), inventory)));
		observers.add(observer);
	}

	public void removeObserver(InventoryObserver observer) {
		observers.remove(observer);
	}

	private void notifyObserversCreation(InventoryHolder inventoryHolder, Inventory inventory) {
		observers.forEach(o -> GameManager.observerExecutor
				.submit(() -> o.createInventory(inventoryHolder.getUUID(), inventory)));
	}

	private void notifyObserversDeletion(Inventory inventory) {
		observers.forEach(o -> GameManager.observerExecutor
				.submit(() -> o.deleteInventory(inventory.getUUID())));
	}

	private void notifyObserversUpdate(Inventory inventory, int slot) {
		observers.forEach(o -> GameManager.observerExecutor.submit(() -> o
				.updateInventory(inventory.getUUID(), slot, inventory.getItem(slot))));
	}

	public void register(InventoryHolder inventoryHolder, Inventory inventory) {
		inventories.put(inventoryHolder, inventory);
		notifyObserversCreation(inventoryHolder, inventory);
	}

	public void unregister(InventoryHolder inventoryHolder) {
		Inventory inventory = inventories.remove(inventoryHolder);
		notifyObserversDeletion(inventory);
	}

	public int getFreeSlots(InventoryHolder inventoryHolder) {
		return inventories.get(inventoryHolder).getFreeSlots();
	}

	public void giveItem(InventoryHolder inventoryHolder, Item item) {
		Inventory inventory = inventories.get(inventoryHolder);
		int slot = inventory.addItem(item);
		notifyObserversUpdate(inventory, slot);
	}

	public boolean hasItem(InventoryHolder inventoryHolder, Item item) {
		return inventories.get(inventoryHolder).getSlot(item) >= 0;
	}

	public Item removeItem(InventoryHolder inventoryHolder, int slot) {
		Inventory inventory = inventories.get(inventoryHolder);
		Item item = inventory.removeItem(slot);
		notifyObserversUpdate(inventory, slot);
		return item;
	}

	public void useItem(InventoryHolder inventoryHolder, int slot, Consumer<Item> useCallback) {
		Inventory inventory = inventories.get(inventoryHolder);
		Item item = inventory.getItem(slot);
		if (item == null) {
			return;
		}

		useCallback.accept(item);
		if (!item.isReusable()) {
			inventory.removeItem(slot);
		}
		notifyObserversUpdate(inventory, slot);
	}
}
