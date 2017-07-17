package com.github.TheDwoon.robots.game;

import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.UUIDGenerator;

import java.util.Arrays;

public class Inventory {
	private final long uuid;
	private final Item[] items;

	public Inventory(final int size) {
		this(UUIDGenerator.obtainUUID(), size);
	}
	
	public Inventory(final long uuid, final int size) {
		this.uuid = uuid;
		this.items = new Item[size];
	}

	public Inventory(final long uuid, final Item[] items) {
	    this.uuid = uuid;
	    this.items = items;
    }

	public final long getUUID() {
		return uuid;
	}

	public Item[] getItems() {
		return Arrays.copyOf(items, items.length);
	}

	public int addItem(final Item item) {
		for (int i = 0; i < size(); i++) {
			if (items[i] == null) {
				items[i] = item;
				return i;
			}
		}

		return -1;
	}

	public void removeItem(int slot) {
		items[slot] = null;
	}

	public Item getItem(int slot) {
		return items[slot];
	}

	public int getSlot(Item item) {
		for (int slot = 0; slot < items.length; slot++) {
			if (item.equals(items[slot])) {
				return slot;
			}
		}
		return -1;
	}

	public int size() {
		return items.length;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Inventory inventory = (Inventory) o;

		return uuid == inventory.uuid;
	}

	@Override
	public int hashCode() {
		return (int) (uuid ^ (uuid >>> 32));
	}
}
