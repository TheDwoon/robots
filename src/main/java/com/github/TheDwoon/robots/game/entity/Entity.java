package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.server.UUIDGenerator;

public abstract class Entity {
	/*
	 * use x, y to make transmitting an entity easier. The client should have the field information
	 * for each field in a separate representation to restrict updates to entities.
	 * Fields, the Board in general, should be a nearly static environment.
	 */	
	private final long uuid;
	private transient Board board;
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

	public final Board getBoard() {
		return board;
	}
	
	public final void setBoard(Board board) {
		this.board = board;
	}
	
	public void update() {
		
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
