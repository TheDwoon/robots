package com.github.TheDwoon.robots.game;

import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.UUIDGenerator;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;

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

	public synchronized int addItem(final Item item) {

		for (int i = 0; i < size(); i++) {
			if (items[i] == null) {
				items[i] = item;
				return i;
			}
		}

		return -1;
	}

	public synchronized Item removeItem(int slot) {
		Item item = items[slot];
		items[slot] = null;
		return item;
	}

	public Item getItem(int slot) {
		return items[slot];
	}

	public synchronized int getSlot(Item item) {
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

	public synchronized int getFreeSlots() {
		int freeSlots = items.length;
		for (Item item: items) {
			if (item != null) {
				freeSlots--;
			}
		}
		return freeSlots;
	}

	public synchronized int getFirstMatchingSlot(Predicate<Item> predicate) {
		for (int i = 0; i < items.length; i++) {
			if (predicate.test(items[i])) {
				return i;
			}
		}
		return -1;
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
