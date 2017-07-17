package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.InventoryHolder;
import com.github.TheDwoon.robots.game.items.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sigma_000 on 17.07.2017.
 */
public class InventoryManager {

    private final Map<InventoryHolder, Inventory> inventories;

    public InventoryManager() {
        this.inventories = new HashMap<>();
    }

    public void register(InventoryHolder inventoryHolder, Inventory inventory) {
        inventories.put(inventoryHolder, inventory);
    }

    public void unregister(InventoryHolder inventoryHolder) {
        inventories.remove(inventoryHolder);
    }

    public boolean hasItem(InventoryHolder inventoryHolder, Item item) {
        return inventories.get(inventoryHolder).getSlot(item) >= 0;
    }
}
