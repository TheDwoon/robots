package com.github.TheDwoon.robots.client.student;

import com.github.TheDwoon.robots.game.field.Facing;
import com.github.TheDwoon.robots.game.field.Field;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.items.Gun;
import com.github.TheDwoon.robots.server.actions.PlayerAction;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.abs;

public class DuellingAI extends AbstractBasicAI {
	private final Facing facing;
	private int lastX;
	private int lastY;

	private LinkedList<PlayerAction> actionQueue;

	public DuellingAI(Facing facing) {
		this.facing = facing;
		lastX = -1;
		lastY = -1;
		actionQueue = new LinkedList<>();
	}

	@Override
	public PlayerAction makeTurn() {
		if (!actionQueue.isEmpty()) {
			return actionQueue.pollFirst();
		}

		// turn to opponent
		Facing currentFacing = getRobot().getFacing();
		if (lastX == -1 && lastY == -1 && currentFacing != this.facing) {
			if (currentFacing.left() == facing) {
				return turnLeft();
			} else {
				return turnRight();
			}
		}

		// pick up weapon
		Field beneath = getBeneath();
		if (beneath.hasItem()) {
			return pickUpItem();
		}

		// spy for enemy
		int gunSlot = getInventory().getFirstMatchingSlot(item -> item instanceof Gun);
		if (gunSlot >= 0) {
			Gun gun = (Gun) getInventory().getItem(gunSlot);
			Stream<Field> fieldStream = getFields().stream();
			switch (currentFacing) {
			case NORTH:
				fieldStream = fieldStream.filter(field -> field.getX() == getRobot().getX()
						&& field.getY() > getRobot().getY());
				break;
			case WEST:
				fieldStream = fieldStream.filter(field -> field.getX() < getRobot().getX()
						&& field.getY() == getRobot().getY());
				break;
			case SOUTH:
				fieldStream = fieldStream.filter(field -> field.getX() == getRobot().getX()
						&& field.getY() < getRobot().getY());
				break;
			case EAST:
				fieldStream = fieldStream.filter(field -> field.getX() > getRobot().getX()
						&& field.getY() == getRobot().getY());
				break;
			}
			List<LivingEntity> opponents = fieldStream.filter(Field::isOccupied).filter(field ->
					abs(field.getX() - getRobot().getX() + field.getY() - getRobot().getY()) <= gun
							.getRange()).map(Field::getOccupant).collect(Collectors.toList());
			if (!opponents.isEmpty()) {
				return useItem(gunSlot);
			}

			if (beneath.getX() == lastX && beneath.getY() == lastY) {
				actionQueue.addLast(turnLeft());
				actionQueue.addLast(driveForward());
				return turnLeft();
			}
		}

		lastX = beneath.getX();
		lastY = beneath.getY();

		// move forward
		return driveForward();
	}
}
