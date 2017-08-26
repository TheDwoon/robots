package com.github.TheDwoon.robots.game.items;

import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.game.board.Field;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.server.ScoreCallback;
import com.github.TheDwoon.robots.server.managers.BoardManager;
import com.github.TheDwoon.robots.server.managers.InventoryManager;

import static java.lang.Math.round;

/**
 * Created by sigma_000 on 18.07.2017.
 */
public class Gun extends Weapon {

	private static final int DEFAULT_RANGE = 1;
	private static final int DEFAULT_DAMAGE = 2;
	private static final double DEFAULT_PIERCING_LOSS = 1;
	private static final int DEFAULT_ROUNDS = 6;

	private int roundsLeft;

	public Gun() {
		super(DEFAULT_RANGE, DEFAULT_DAMAGE, DEFAULT_PIERCING_LOSS);
		roundsLeft = DEFAULT_ROUNDS;
	}

	public Gun(int x, int y) {
		super(x, y, DEFAULT_RANGE, DEFAULT_DAMAGE, DEFAULT_PIERCING_LOSS);
		roundsLeft = DEFAULT_ROUNDS;
	}

	public Gun(long uuid, int x, int y, int range, int damage, double piercingLoss,
			int roundsLeft) {
		super(uuid, x, y, range, damage, piercingLoss);
		this.roundsLeft = roundsLeft;
	}

	@Override
	public void use(Robot robot, BoardManager boardManager, InventoryManager inventoryManager,
			ScoreCallback scoreCallback) {
		if (roundsLeft <= 0) {
			return;
		}

		Facing facing = robot.getFacing();

		double remainingDamage = getDamage();
		for (int i = 1; i <= getRange(); i++) {
			Field field = boardManager
					.getFieldChecked(robot.getX() + i * facing.dx, robot.getY() + i * facing.dy);
			if (field == null) {
				break;
			}

			if (field.isOccupied()) {
				boardManager.damageLivingEntity(field.getX(), field.getY(),
						(int) round(remainingDamage), robot);
				remainingDamage *= (1 - getPiercingLoss());
				if (round(remainingDamage) <= 0) {
					break;
				}
			}
		}

		if (roundsLeft != Integer.MAX_VALUE) {
			roundsLeft--;
			System.out.printf("%d rounds left%n", roundsLeft);
		}
	}

	@Override
	public boolean isReusable() {
		return roundsLeft > 0;
	}

	public int getRoundsLeft() {
		return roundsLeft;
	}
}
