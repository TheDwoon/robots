package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.server.UUIDGenerator;

public abstract class EntityImpl implements Entity {

	private final long uuid;
	private int x;
	private int y;
	
	
	public EntityImpl(int x, int y) {
		this(UUIDGenerator.obtainUUID(), x, y);
	}
	
	public EntityImpl(long uuid, int x, int y) {
		this.uuid = uuid;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public final long getUUID() {
		return uuid;
	}
	
	@Override
	public final int getX() {
		return x;
	}

	@Override
	public final void setX(int x) {
		this.x = x;
	}

	@Override
	public final int getY() {
		return y;
	}

	@Override
	public final void setY(int y) {
		this.y = y;
	}

	@Override
	public final void setPosition(int x, int y) {
		setX(x);
		setY(y);
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !Entity.class.isAssignableFrom(o.getClass()))
			return false;

		Entity entity = (Entity) o;

		return uuid == entity.getUUID();
	}

	@Override
	public int hashCode() {
		return (int) (uuid ^ (uuid >>> 32));
	}

}
