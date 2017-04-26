package com.github.TheDwoon.robots.game;

import java.util.Objects;

public class Field {
	private final int x;
	private final int y;
	// material is also a hint to the client what texture to use.
	private final Material material;

	public Field(final int x, final int y, Material material) {	
		this.x = x;
		this.y = y;
		this.material = Objects.requireNonNull(material);		
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
	
	@Override
	public String toString() {
		return String.format("[(%d,%d)/%s]", x, y, material.name());
	}

}
