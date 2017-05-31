package com.github.TheDwoon.robots.server.entity;

import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.server.RobotsServer;

public class ServerLivingEntity extends ServerEntity implements LivingEntity {
	private final LivingEntity entity;
	
	public ServerLivingEntity(RobotsServer server, LivingEntity entity) {
		super(server, entity);
		
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
		pushEntityUpdate();
	}

	@Override
	public void damage(int damage) {
		entity.damage(damage);
		
		if (damage != 0) {
			pushEntityUpdate();
		}
	}

	@Override
	public boolean isAlive() {
		return entity.isAlive();
	}

	@Override
	public boolean isDead() {
		return entity.isDead();
	}
	
	private void pushEntityUpdate() {
		// TODO (danielw, 31.05.2017): maybe make a explicit HealthUpdate-Method. (Wasting a lot of performance to a little int change)
		getServer().getEntityBroadcaster().updateEntity(entity);
	}
	
	public final LivingEntity getLivingEntity() {
		return entity;
	}
}