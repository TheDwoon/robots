package com.github.TheDwoon.robots.game.items;

import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.server.managers.BoardManager;
import com.github.TheDwoon.robots.server.managers.InventoryManager;

import static java.lang.Math.*;

public class Bomb extends Weapon {

	private static final int DEFAULT_RANGE = 2;
	private static final int DEFAULT_DAMAGE = 2;
	private static final double DEFAULT_PIERCING_LOSS = .5;

	public Bomb() {
		super(DEFAULT_RANGE, DEFAULT_DAMAGE, DEFAULT_PIERCING_LOSS);
	}

	public Bomb(int x, int y) {
		super(x, y, DEFAULT_RANGE, DEFAULT_DAMAGE, DEFAULT_PIERCING_LOSS);
	}

	public Bomb(long uuid, int x, int y, int range, int damage, double piercingLoss) {
		super(uuid, x, y, range, damage, piercingLoss);
	}

	@Override
	public void use(Robot robot, BoardManager boardManager, InventoryManager inventoryManager) {
		int posX = robot.getX();
		int posY = robot.getY();
		for (int x = max(posX - getRange(), 0);
			 x <= min(posX + getRange(), boardManager.getWidth() - 1); x++) {
			int dx = abs(x - posX);
			for (int y = max(posY - getRange(), 0);
				 y <= min(posY + getRange(), boardManager.getHeight() - 1); y++) {
				int dy = abs(y - posY);
				int damage = (int) round(
						getDamage() * pow((1 - getPiercingLoss()), sqrt(dx * dx + dy * dy)));
				if (damage > 0) {
					boardManager.damageLivingEntity(x, y, damage);
				}
			}
		}
	}
}
