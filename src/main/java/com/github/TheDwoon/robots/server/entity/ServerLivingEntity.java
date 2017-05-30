package com.github.TheDwoon.robots.server.entity;

import com.github.TheDwoon.robots.game.entity.LivingEntity;

public class ServerLivingEntity extends ServerEntity implements LivingEntity {
	private final LivingEntity entity;
	
	public ServerLivingEntity(LivingEntity entity) {
		super(entity);
		
		this.entity = entity;
	}
	
	@Override
	public int getMaxHealth() {
		return entity.getMaxHealth();
	}

	@Override
	public int getHealth() {
		return entity.getHealth();
	}

	@Override
	public void setHealth(int health) {
		entity.setHealth(health);
	}

	@Override
	public void damage(int damage) {
		entity.damage(damage);
	}

	@Override
	public boolean isAlive() {
		return entity.isAlive();
	}

	@Override
	public boolean isDead() {
		return entity.isDead();
	}
	
	public LivingEntity getLivingEntity() {
		return entity;
	}
}
