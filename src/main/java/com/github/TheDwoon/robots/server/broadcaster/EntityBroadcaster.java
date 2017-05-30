package com.github.TheDwoon.robots.server.broadcaster;

import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.interaction.EntityObserver;

public final class EntityBroadcaster extends Broadcaster<EntityObserver> implements EntityObserver {

	public EntityBroadcaster() {
		super();
	}
	
	@Override
	public void spawnEntity(Entity entity) {
		notifyObservers(o -> o.spawnEntity(entity));
	}

	@Override
	public void spawnRobot(Robot robot) {
		notifyObservers(o -> o.spawnRobot(robot));
	}

	@Override
	public void removeEntity(long uuid) {
		notifyObservers(o -> o.removeEntity(uuid));
	}

	@Override
	public void updateEntity(Entity entity) {
		notifyObservers(o -> o.updateEntity(entity));
	}

	@Override
	public void updateLocation(long uuid, int x, int y) {
		notifyObservers(o -> o.updateLocation(uuid, x, y));
	}
}
