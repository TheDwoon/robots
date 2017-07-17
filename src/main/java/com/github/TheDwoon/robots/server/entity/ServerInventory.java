package com.github.TheDwoon.robots.server.entity;

import java.util.Arrays;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.RobotsServer;

/**
 * Created by sigma_000 on 30.05.2017.
 */
public class ServerInventory {
	public static final int DEFAULT_SIZE = 12;
	
	private final RobotsServer server;
    private final Inventory inventory;

    private final ServerItem[] items;
    
    public ServerInventory(RobotsServer server) {
    	this(server, new Inventory(DEFAULT_SIZE));
    }
    
    public ServerInventory(RobotsServer server, Inventory inventory) {
        this.inventory = inventory;
        this.server = server;
        this.items = new ServerItem[inventory.size()];
        
        // inform clients about this inventory (the contained inventory!)
        getServer().getInventoryBroadcaster().createInventory(inventory);
    }

    public long getUUID() {
        return inventory.getUUID();
    }

    public ServerItem[] getServerItems() {
    	return Arrays.copyOf(items, items.length);
    }
    
    public Item[] getItems() {
        return inventory.getItems();
    }

    public int addItem(ServerItem item) {
        int slot = inventory.addItem(item.getItem());        
        
        if (slot >= 0) {
        	items[slot] = item;
    		getServer().getInventoryBroadcaster().updateInventory(inventory.getUUID(), slot, item.getItem());
        }
        
        return slot;
    }

    public void removeItem(int slot) {
    	items[slot] = null;
        inventory.removeItem(slot);
        
        getServer().getInventoryBroadcaster().updateInventory(inventory.getUUID(), slot, null);
    }

    public ServerItem getItem(int slot) {
        return items[slot];
    }

    public int size() {
        return inventory.size();
    }

    public boolean equals(Object obj) {
        return inventory.equals(obj);
    }

    public int hashCode() {
        return inventory.hashCode();
    }
    
    public final RobotsServer getServer() {
		return server;
	}
}
