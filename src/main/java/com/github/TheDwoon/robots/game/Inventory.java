package com.github.TheDwoon.robots.game;

import java.util.Arrays;

import com.github.TheDwoon.robots.game.items.Item;

public class Inventory {

	private final Item[] items;

	public Inventory(final int size) {
		this.items = new Item[size];
	}

	public Item[] getItems() {
		return Arrays.copyOf(items, items.length);
	}

	public void addItem(final Item item) {
		// TODO: implement network
	}
}
