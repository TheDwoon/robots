package com.github.TheDwoon.robots.game.entity;

public abstract class LivingEntity extends Entity {

	private final int maxHealth;
	private int health;
	
	public LivingEntity(int x, int y, int maxHealth) {
		super(x, y);		
		
		this.maxHealth = maxHealth;
		health = 0;
	}

	public LivingEntity(long uuid, int x, int y, int maxHealth, int health) {
		super(uuid, x, y);
		this.maxHealth = maxHealth;
		this.health = health;
	}
	
	public final int getMaxHealth() {
		return maxHealth;
	}
	
	public final int getHealth() {
		return health;
	}
	
	public final void setHealth(int health) {
		this.health = health;
	}
	
	public final void damage(int damage) {
		// yes, negative damage is a heal.
		health = Math.min(health + damage, maxHealth); 
	}
	
	public final boolean isAlive() {
		return health > 0;
	}
	
	public final boolean isDead() {
		return !isAlive();
	}
}
