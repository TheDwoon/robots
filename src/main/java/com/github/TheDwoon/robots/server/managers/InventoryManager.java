package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.InventoryHolder;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.game.items.Item;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

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
        GameManager.oberverExecutor.submit(() -> inventories.forEach((inventoryHolder, inventory) -> observer.createInventory(inventoryHolder.getUUID(), inventory)));
        observers.add(observer);
    }

    public void removeObserver(InventoryObserver observer) {
        observers.remove(observer);
    }

    private void notifyObserversCreation(InventoryHolder inventoryHolder, Inventory inventory) {
        GameManager.oberverExecutor.submit(() -> observers.forEach(o -> o.createInventory(inventoryHolder.getUUID(), inventory)));
    }

    private void notifyObserversDeletion(Inventory inventory) {
        GameManager.oberverExecutor.submit(() -> observers.forEach(o -> o.deleteInventory(inventory.getUUID())));
    }

    private void notifyObserversUpdate(Inventory inventory, int slot) {
        GameManager.oberverExecutor.submit(() -> observers.forEach(o -> o.updateInventory(inventory.getUUID(), slot, inventory.getItem(slot))));
    }

    public void register(InventoryHolder inventoryHolder, Inventory inventory) {
        inventories.put(inventoryHolder, inventory);
        notifyObserversCreation(inventoryHolder, inventory);
    }

    public void unregister(InventoryHolder inventoryHolder) {
        Inventory inventory = inventories.remove(inventoryHolder);
        notifyObserversDeletion(inventory);
    }

    public boolean hasItem(InventoryHolder inventoryHolder, Item item) {
        return inventories.get(inventoryHolder).getSlot(item) >= 0;
    }

    public int getFreeSlots(InventoryHolder inventoryHolder) {
        return inventories.get(inventoryHolder).getFreeSlots();
    }

    public void giveItem(InventoryHolder inventoryHolder, Item item) {
        Inventory inventory = inventories.get(inventoryHolder);
        int slot;
        synchronized (inventory) {
            slot = inventory.addItem(item);
        }
        notifyObserversUpdate(inventory, slot);
    }
}
