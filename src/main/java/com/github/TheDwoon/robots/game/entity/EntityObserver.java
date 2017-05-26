package com.github.TheDwoon.robots.game.entity;

public interface EntityObserver {
	void spawnEntity(Entity entity);
	void spawnRobot(Robot robot);
	void removeEntity(long uuid);
	void updateEntity(Entity entity);
	void updateLocation(long uuid, int x, int y);
}