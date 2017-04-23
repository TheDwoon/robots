package com.github.TheDwoon.robots.game;

import com.github.TheDwoon.robots.game.items.Item;

public class Field {

	private final int x;
	private final int y;

	private Item item;
	private final boolean visitable;
	private Robot visitor;

	public Field(final int x, final int y, final boolean visitable) {
		this(x, y, null, visitable, null);
	}

	public Field(final int x, final int y, final Item item, final boolean visitable,
		final Robot visitor) {
		this.x = x;
		this.y = y;
		this.item = item;
		this.visitable = visitable;
		this.visitor = visitor;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(final Item item) {
		this.item = item;
	}

	public Robot getVisitor() {
		return visitor;
	}

	public void setVisitor(final Robot visitor) {
		this.visitor = visitor;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isVisitable() {
		return visitable;
	}

}
