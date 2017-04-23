package com.github.TheDwoon.robots.game;

import java.util.LinkedList;
import java.util.List;

import com.github.TheDwoon.robots.game.items.Item;

public class Inventory {

	private final int size;
	private final List<Item> items;

	public Inventory(final int size) {
		this.size = size;
		items = new LinkedList<>();
	}

	public Item[] getItems() {
		return items.toArray(new Item[items.size()]);
	}

	public void addItem(final Item item) {
		// TODO: implement network
	}

	public boolean contains(final Item item) {
		return items.contains(item);
	}

}
