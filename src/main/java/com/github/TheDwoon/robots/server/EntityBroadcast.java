package com.github.TheDwoon.robots.server;

import java.util.ArrayList;
import java.util.List;

import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.EntityObserver;

public final class EntityBroadcast implements EntityObserver {
	public static final EntityBroadcast INSTANCE = new EntityBroadcast();
	
	private final List<EntityObserver> observers;
	
	private EntityBroadcast() {
		this.observers = new ArrayList<>(32);
	}
	
	public void registerObserver(EntityObserver o) {
		if (o != null) {
			synchronized (observers) {
				observers.add(o);
			}
		}
	}
	
	public void removeObserver(EntityObserver o) {
		if (o != null) {
			synchronized (observers) {
				observers.remove(o);
			}
		}
	}
	
	@Override
	public void spawnEntity(final Entity entity) {
		synchronized (observers) {
			observers.forEach(o -> o.spawnEntity(entity));
		}
	}

	@Override
	public void removeEntity(final long uuid) {
		synchronized (observers) {
			observers.forEach(o -> o.removeEntity(uuid));
		}
	}

	@Override
	public void updateEntity(final Entity entity) {
		synchronized (observers) {
			observers.forEach(o -> o.updateEntity(entity));
		}
	}

	@Override
	public void updateLocation(final long uuid, final int x, final int y) {
		synchronized (observers) {
			observers.forEach(o -> o.updateLocation(uuid, x, y));
		}
	}
}
