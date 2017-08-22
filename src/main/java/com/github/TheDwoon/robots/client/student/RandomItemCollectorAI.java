package com.github.TheDwoon.robots.client.student;

import com.github.TheDwoon.robots.game.board.Field;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.actions.PlayerAction;

import java.util.Random;

public class RandomItemCollectorAI extends AbstractBasicAI {
	private final Random random = new Random();
	private final PlayerAction[] movementActions =
			new PlayerAction[] { driveForward(), driveBackward(), turnLeft(), turnRight() };

	private Field lastDropPoint;

	public RandomItemCollectorAI() {
		lastDropPoint = null;
	}

	@Override
	public PlayerAction makeTurn() {
		Field beneath = getBeneath();
		if (!beneath.equals(lastDropPoint) && beneath.hasItem()) {
			lastDropPoint = null;
			return pickUpItem();
		}

		Item[] items = getInventory().getItems();
		for (int slot = 0; slot < items.length; slot++) {
			if (items[slot] != null && random.nextDouble() < .005) {
				lastDropPoint = beneath;
				return dropItem(slot);
			}
		}
		return movementActions[random.nextInt(movementActions.length)];
	}
}
