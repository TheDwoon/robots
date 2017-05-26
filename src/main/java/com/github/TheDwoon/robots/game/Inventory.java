package com.github.TheDwoon.robots.game;

import java.util.Arrays;

import com.github.TheDwoon.robots.game.items.Item;

public class Inventory {
	private static volatile Long uuidCounter = new Long(1);
	
	private final long uuid;
	private final Item[] items;	
	
	public Inventory(final long uuid, final int size) {
		this.uuid = uuid;
		this.items = new Item[size];
	}

	public final long getUUID() {
		return uuid;
	}
	
	public Item[] getItems() {
		return Arrays.copyOf(items, items.length);
	}

	public boolean addItem(final Item item) {
		for (int i = 0; i < size(); i++) {
			if (items[i] == null) {
				items[i] = item;
				return true;
			}
		}
		
		return false;
	}
	
	public void removeItem(int slot) {
		items[slot] = null;
	}
	
	public Item getItem(int slot) {
		return items[slot];
	}
	
	public int size() {
		return items.length;
	}
}
