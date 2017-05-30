package com.github.TheDwoon.robots.server.entity;

import com.github.TheDwoon.robots.game.entity.Entity;

public abstract class ServerEntity implements Entity {
	private final Entity entity;
	
	public ServerEntity(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public long getUUID() {
		return entity.getUUID();
	}

	@Override
	public int getX() {
		return entity.getX();
	}

	@Override
	public void setX(int x) {
		entity.setX(x);
	}

	@Override
	public int getY() {
		return entity.getY();
	}

	@Override
	public void setY(int y) {
		entity.setY(y);
	}

	@Override
	public void setPosition(int x, int y) {
		entity.setPosition(x, y);
	}

	@Override
	public void update() {

	}
	
	public Entity getEntity() {
		return entity;
	}
}
