package com.github.TheDwoon.robots.game.board;

import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.items.Item;

import java.util.Objects;

public class Field {
	private final int x;
	private final int y;
	// material is also a hint to the client what texture to use.
	private Material material;

	private LivingEntity occupant;
	private Item item;

	public Field(final int x, final int y, Material material) {
		this(x, y, material, null, null);
	}

	public Field(final int x, final int y, Material material, LivingEntity occupant, Item item) {
		this.x = x;
		this.y = y;
		this.material = Objects.requireNonNull(material);

		setOccupant(occupant);
		setItem(item);
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

	public final void setMaterial(Material material) {
		this.material = Objects.requireNonNull(material);
	}
	
	public final LivingEntity getOccupant() {
		return occupant;
	}

	public final void setOccupant(LivingEntity occupant) {
		if (occupant != null) {
			occupant.setPosition(x, y);
			this.occupant = occupant;
		} else {
			this.occupant = null;
		}
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
		if (item != null) {
			item.setPosition(x, y);
			this.item = item;
		} else {
			this.item = null;
		}
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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Field field = (Field) o;

		if (x != field.x)
			return false;
		if (y != field.y)
			return false;
		return material == field.material;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		result = 31 * result + material.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return String.format("[(%d,%d)/%s]", x, y, material.name());
	}

}
