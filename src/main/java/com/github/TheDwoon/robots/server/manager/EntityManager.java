package com.github.TheDwoon.robots.server.manager;

import com.github.TheDwoon.robots.game.entity.Entity;

public class EntityManager<T extends Entity> {
	private final T entity;

	public EntityManager(T entity) {
		this.entity = entity;
	}
	
	public T getEntity() {
		return entity;
	}
}
