package com.github.TheDwoon.robots.game.entity;

/**
 * Created by sigma_000 on 30.05.2017.
 */
public interface LivingEntity extends Entity {
    int getMaxHealth();

    int getHealth();

    void setHealth(int health);

    void damage(int damage);

    boolean isAlive();

    boolean isDead();
}
