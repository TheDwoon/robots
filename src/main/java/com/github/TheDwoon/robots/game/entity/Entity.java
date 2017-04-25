package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Field;

public abstract class Entity {

	private Field position;

	public Entity(final Field position) {
		this.position = position;
	}

	public Field getPosition() {
		return position;
	}

	// TODO (sigmarw, 25.04.2017): maybe not public?
	public void setPosition(final Field position) {
		this.position = position;
	}

	public String getType() {
		return getClass().getSimpleName();
	}
}
