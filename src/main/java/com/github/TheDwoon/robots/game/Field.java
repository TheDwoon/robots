package com.github.TheDwoon.robots.game;

import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.items.Item;

public class Field {

	private final int x;
	private final int y;

	private Item item;
	private final boolean visitable;
	private Entity visitor;

	public Field(final int x, final int y, final boolean visitable) {
		this(x, y, null, visitable, null);
	}

	public Field(final int x, final int y, final Item item, final boolean visitable,
		final Entity visitor) {
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

	public Entity getVisitor() {
		return visitor;
	}

	public void setVisitor(final Entity visitor) {
		// TODO (sigmarw, 25.04.2017): maybe add plausibility checks
		visitor.setPosition(this);
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

	@Override
	public String toString() {
		return String.format("[%c/%c]", visitor != null ? visitor.getType().charAt(0) : ' ',
			item != null ? item.getType().charAt(0) : ' ');
	}

}
