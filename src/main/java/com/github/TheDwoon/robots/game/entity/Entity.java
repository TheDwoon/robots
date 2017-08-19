package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.UniquelyIdentifiable;
import com.github.TheDwoon.robots.server.UUIDGenerator;

public abstract class Entity implements UniquelyIdentifiable {

	private final long uuid;
	private int x;
	private int y;

	public Entity(int x, int y) {
		this(UUIDGenerator.obtainUUID(), x, y);
	}

	public Entity(long uuid, int x, int y) {
		this.uuid = uuid;
		this.x = x;
		this.y = y;
	}

	@Override
	public final long getUUID() {
		return uuid;
	}

	public final int getX() {
		return x;
	}

	public final void setX(int x) {
		this.x = x;
	}

	public final int getY() {
		return y;
	}

	public final void setY(int y) {
		this.y = y;
	}

	public final void setPosition(int x, int y) {
		setX(x);
		setY(y);
	}

	public String getType() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Entity entity = (Entity) o;

		return uuid == entity.uuid;
	}

	@Override
	public int hashCode() {
		return (int) (uuid ^ (uuid >>> 32));
	}
}
