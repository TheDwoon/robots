package com.github.TheDwoon.robots.server.entity;

import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.server.RobotsServer;

public abstract class ServerEntity {
	private final Entity entity;
	private final RobotsServer server;
	
	public ServerEntity(RobotsServer server, Entity entity) {
		this.server = server;
		this.entity = entity;
	}
	
	public long getUUID() {
		return entity.getUUID();
	}

	public int getX() {
		return entity.getX();
	}

	public void setX(int x) {
		entity.setX(x);
		pushLocationUpdate();
	}

	public int getY() {
		return entity.getY();
	}

	public void setY(int y) {
		entity.setY(y);
		pushLocationUpdate();
	}

	public void setPosition(int x, int y) {
		entity.setPosition(x, y);
		pushLocationUpdate();
	}

	private void pushLocationUpdate() {
		getServer().getEntityBroadcaster().updateLocation(entity.getUUID(), entity.getX(), entity.getY());
	}
	
	public void update() {

	}
	
	public final Entity getEntity() {
		return entity;
	}
	
	public final RobotsServer getServer() {
		return server;
	}
}
