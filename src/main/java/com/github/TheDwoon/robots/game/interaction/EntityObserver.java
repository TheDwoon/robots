package com.github.TheDwoon.robots.game.interaction;

import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;

public interface EntityObserver {
	void spawnEntity(Entity entity);
	void spawnRobot(Robot robot);
	void removeEntity(long uuid);
	void updateEntity(Entity entity);
	void updateLocation(long uuid, int x, int y);
}
