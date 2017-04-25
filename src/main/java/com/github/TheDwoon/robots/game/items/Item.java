package com.github.TheDwoon.robots.game.items;

public abstract class Item {
	private final long uuid;

	public Item(final long uuid) {
		this.uuid = uuid;
	}

	public String getType() {
		return getClass().getSimpleName();
	}

}
