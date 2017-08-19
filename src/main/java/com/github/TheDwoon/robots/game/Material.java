package com.github.TheDwoon.robots.game;

/**
 * This enum represents the materials known to the game.
 *
 * @author TheDwoon
 */
public enum Material {
	VOID(false), GRASS(true), TREE(false), ROCK(false), WATER(false), SPAWN(true);

	private final boolean visitable;

	private Material(boolean visitiable) {
		this.visitable = visitiable;
	}

	public final boolean isVisitable() {
		return visitable;
	}
}
