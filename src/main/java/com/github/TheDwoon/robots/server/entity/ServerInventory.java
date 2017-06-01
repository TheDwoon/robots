package com.github.TheDwoon.robots.server.entity;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.InventoryImpl;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.RobotsServer;

/**
 * Created by sigma_000 on 30.05.2017.
 */
public class ServerInventory implements Inventory {
	public static final int DEFAULT_SIZE = 12;
	
	private final RobotsServer server;
    private final Inventory inventory;

    public ServerInventory(RobotsServer server) {
    	this(server, new InventoryImpl(DEFAULT_SIZE));
    }
    
    public ServerInventory(RobotsServer server, Inventory inventory) {
        this.inventory = inventory;
        this.server = server;
        
        // inform clients about this inventory (the contained inventory!)
        getServer().getInventoryBroadcaster().createInventory(inventory);
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
    public int addItem(Item item) {
        int slot = inventory.addItem(item);
        
        if (slot >= 0) {
        	// make sure to transmit a non ServerItem
        	if (item instanceof ServerItem) {
        		getServer().getInventoryBroadcaster().updateInventory(inventory.getUUID(), slot, ((ServerItem) item).getItem());
        	} else {
        		getServer().getInventoryBroadcaster().updateInventory(inventory.getUUID(), slot, item);
        	}
        }
        
        return slot;
    }

    @Override
    public void removeItem(int slot) {
        inventory.removeItem(slot);
        
        getServer().getInventoryBroadcaster().updateInventory(inventory.getUUID(), slot, null);
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
    
    public final RobotsServer getServer() {
		return server;
	}
}
