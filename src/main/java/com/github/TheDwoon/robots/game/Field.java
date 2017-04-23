package com.github.TheDwoon.robots.game;

import com.github.TheDwoon.robots.game.items.Item;

public class Field {
	private Item item;
	private final boolean visitable;
	private Robot visitor;

	public Field(final Item item, final boolean visitable, final Robot visitor) {
		this.item = item;
		this.visitable = visitable;
		this.visitor = visitor;
	}

}
