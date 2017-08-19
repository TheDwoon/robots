package com.github.TheDwoon.robots.game.items;

/**
 * Created by sigma_000 on 18.07.2017.
 */
public abstract class Weapon extends Item {

	private final int range;
	private final int damage;
	private final double piercingLoss;

	public Weapon(int range, int damage, double piercingLoss) {
		super();
		this.range = range;
		this.damage = damage;
		this.piercingLoss = piercingLoss;
	}

	public Weapon(int x, int y, int range, int damage, double piercingLoss) {
		super(x, y);
		this.range = range;
		this.damage = damage;
		this.piercingLoss = piercingLoss;
	}

	public Weapon(long uuid, int x, int y, int range, int damage, double piercingLoss) {
		super(uuid, x, y);
		this.range = range;
		this.damage = damage;
		this.piercingLoss = piercingLoss;
	}

	public int getRange() {
		return range;
	}

	public int getDamage() {
		return damage;
	}

	public double getPiercingLoss() {
		return piercingLoss;
	}
}
