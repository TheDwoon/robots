package com.github.TheDwoon.robots.game.items.weapons;

import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.game.items.ItemImpl;

public abstract class Weapon extends ItemImpl {
	public Weapon() {
		super();
	}

	public abstract void hit(LivingEntity entity);
	
	public abstract int getDamage();
	
	public abstract void shoot();
	
	public abstract int getRange();

	public abstract boolean isPiercing();
}
