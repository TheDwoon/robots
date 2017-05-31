package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Facing;

/**
 * Created by sigma_000 on 30.05.2017.
 */
public interface LivingEntity extends Entity {
	Facing getFacing();
	
	void setFacing(Facing facing);
	
    int getMaxHealth();

    int getHealth();

    void setHealth(int health);

    void damage(int damage);

    boolean isAlive();

    boolean isDead();
}
