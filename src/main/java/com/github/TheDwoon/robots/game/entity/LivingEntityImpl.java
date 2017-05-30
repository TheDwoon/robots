package com.github.TheDwoon.robots.game.entity;

public abstract class LivingEntityImpl extends EntityImpl implements LivingEntity {

	private final int maxHealth;
	private int health;
	
	public LivingEntityImpl(int x, int y, int maxHealth) {
		super(x, y);		
		
		this.maxHealth = maxHealth;
		health = 0;
	}

	public LivingEntityImpl(long uuid, int x, int y, int maxHealth, int health) {
		super(uuid, x, y);
		this.maxHealth = maxHealth;
		this.health = health;
	}
	
	@Override
	public final int getMaxHealth() {
		return maxHealth;
	}
	
	@Override
	public final int getHealth() {
		return health;
	}
	
	@Override
	public final void setHealth(int health) {
		this.health = health;
	}
	
	@Override
	public final void damage(int damage) {
		// yes, negative damage is a heal.
		health = Math.min(health + damage, maxHealth); 
	}
	
	@Override
	public final boolean isAlive() {
		return health > 0;
	}
	
	@Override
	public final boolean isDead() {
		return !isAlive();
	}
}
