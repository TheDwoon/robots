package com.github.TheDwoon.robots.game;

import java.util.Arrays;

import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.UUIDGenerator;

public class InventoryImpl implements Inventory {
	private final long uuid;
	private final Item[] items;

	public InventoryImpl(final int size) {
		this(UUIDGenerator.obtainUUID(), size);
	}
	
	public InventoryImpl(final long uuid, final int size) {
		this.uuid = uuid;
		this.items = new Item[size];
	}

	public InventoryImpl(final long uuid, final Item[] items) {
	    this.uuid = uuid;
	    this.items = items;
    }

	@Override
	public final long getUUID() {
		return uuid;
	}

	@Override
	public Item[] getItems() {
		return Arrays.copyOf(items, items.length);
	}

	@Override
	public int addItem(final Item item) {
		for (int i = 0; i < size(); i++) {
			if (items[i] == null) {
				items[i] = item;
				return i;
			}
		}

		return -1;
	}

	@Override
	public void removeItem(int slot) {
		items[slot] = null;
	}

	@Override
	public Item getItem(int slot) {
		return items[slot];
	}

	@Override
	public int size() {
		return items.length;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !Inventory.class.isAssignableFrom(o.getClass()))
			return false;

		Inventory inventory = (Inventory) o;

		return uuid == inventory.getUUID();
	}

	@Override
	public int hashCode() {
		return (int) (uuid ^ (uuid >>> 32));
	}
}
