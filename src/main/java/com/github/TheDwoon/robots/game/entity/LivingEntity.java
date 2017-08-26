package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.board.Facing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.max;
import static java.lang.Math.min;

public abstract class LivingEntity extends Entity {

	private static final Logger log = LogManager.getLogger();

	private final int maxHealth;
	private int health;
	private Facing facing;

	public LivingEntity(int x, int y, int maxHealth) {
		super(x, y);

		this.maxHealth = maxHealth;
		health = maxHealth;
		facing = Facing.NORTH;
	}

	public LivingEntity(long uuid, int x, int y, int maxHealth, int health, Facing facing) {
		super(uuid, x, y);
		this.maxHealth = maxHealth;
		this.health = health;
		this.facing = facing;
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
		if (damage < 0) {
			throw new IllegalArgumentException("Negative damage is not permitted!");
		}
		health = max(health - damage, 0);
	}

	public final void heal(int heal) {
		if (heal < 0) {
			throw new IllegalArgumentException("Negative heal is not permitted!");
		}
		health = min(health + heal, maxHealth);
	}

	/**
	 * Heals the {@link LivingEntity} to its maximum health.
	 */
	public final void fullHeal() {
		health = maxHealth;
	}

	public final boolean isAlive() {
		return health > 0;
	}

	public final boolean isDead() {
		return !isAlive();
	}

	public Facing getFacing() {
		return facing;
	}

	public void setFacing(Facing facing) {
		this.facing = facing;
	}
}
