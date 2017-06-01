package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Facing;

public abstract class LivingEntityImpl extends EntityImpl implements LivingEntity {

    private final int maxHealth;
    private int health;
    private Facing facing;

    public LivingEntityImpl(int x, int y, int maxHealth) {
        super(x, y);

        this.maxHealth = maxHealth;
        health = 0;
        facing = Facing.NORTH;
    }

    public LivingEntityImpl(long uuid, int x, int y, int maxHealth, int health, Facing facing) {
        super(uuid, x, y);
        this.maxHealth = maxHealth;
        this.health = health;
        this.facing = facing;
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

    @Override
    public Facing getFacing() {
        return facing;
    }

    @Override
    public void setFacing(Facing facing) {
        this.facing = facing;
    }
}
