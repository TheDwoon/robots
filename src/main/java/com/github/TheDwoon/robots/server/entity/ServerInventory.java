package com.github.TheDwoon.robots.server.entity;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.items.Item;

/**
 * Created by sigma_000 on 30.05.2017.
 */
public class ServerInventory implements Inventory {

    private final Inventory inventory;

    public ServerInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public long getUUID() {
        return inventory.getUUID();
    }

    @Override
    public Item[] getItems() {
        return inventory.getItems();
    }

    @Override
    public boolean addItem(Item item) {
        return inventory.addItem(item);
    }

    @Override
    public void removeItem(int slot) {
        inventory.removeItem(slot);
    }

    @Override
    public Item getItem(int slot) {
        return inventory.getItem(slot);
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean equals(Object obj) {
        return inventory.equals(obj);
    }

    @Override
    public int hashCode() {
        return inventory.hashCode();
    }
}
