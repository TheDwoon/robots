package com.github.TheDwoon.robots.game.entity;

public abstract class Entity {
	/*
	 * use x, y to make transmitting an entity easier. The client should have the field information
	 * for each field in a separate representation to restrict updates to entities.
	 * Fields, the Board in general, should be a nearly static environment.
	 */
	private static volatile Long uuidCounter = new Long(1);
	
	private final long uuid;
	private int x;
	private int y;

	public Entity(int x, int y) {
		synchronized (uuidCounter) {
			// generate a unique uuid
			this.uuid = uuidCounter++;
		}
		this.x = x;
		this.y = y;
	}

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
	public final boolean equals(Object obj) {
		// entities are only equals if their uuid matches.
		if (obj instanceof Entity)
			return this.uuid == ((Entity) obj).uuid;
		
		return false;
	}
}
