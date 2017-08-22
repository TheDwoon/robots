package com.github.TheDwoon.robots.game.field;

import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.items.Item;

import java.util.Objects;

public class Field {
	private final int x;
	private final int y;
	// material is also a hint to the client what texture to use.
	private final Material material;

	private LivingEntity occupant;
	private Item item;

	public Field(final int x, final int y, Material material) {
		this(x, y, material, null, null);
	}

	public Field(final int x, final int y, Material material, LivingEntity occupant, Item item) {
		this.x = x;
		this.y = y;
		this.material = Objects.requireNonNull(material);

		this.occupant = occupant;
		this.item = item;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public final Material getMaterial() {
		return material;
	}

	public final LivingEntity getOccupant() {
		return occupant;
	}

	public final void setOccupant(LivingEntity occupant) {
		occupant.setPosition(x, y);
		this.occupant = occupant;
	}

	public final LivingEntity removeOccupant() {
		LivingEntity occupant = this.occupant;
		this.occupant = null;
		return occupant;
	}

	public final boolean isOccupied() {
		return occupant != null;
	}

	public final Item getItem() {
		return item;
	}

	public final void setItem(Item item) {
		item.setPosition(x, y);
		this.item = item;
	}

	public final Item removeItem() {
		Item item = this.item;
		this.item = null;
		return item;
	}

	public final boolean hasItem() {
		return item != null;
	}

	public final boolean isVisitable() {
		return material.isVisitable();
	}

	@Override
	public String toString() {
		return String.format("[(%d,%d)/%s]", x, y, material.name());
	}

}
