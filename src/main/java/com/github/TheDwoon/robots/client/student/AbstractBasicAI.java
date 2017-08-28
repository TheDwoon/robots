package com.github.TheDwoon.robots.client.student;

import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.game.interaction.AI;
import com.github.TheDwoon.robots.game.board.Field;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Inventory;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.actions.NoAction;
import com.github.TheDwoon.robots.server.actions.item.DropItem;
import com.github.TheDwoon.robots.server.actions.item.PickUpItem;
import com.github.TheDwoon.robots.server.actions.item.UseItem;
import com.github.TheDwoon.robots.server.actions.movement.DriveBackward;
import com.github.TheDwoon.robots.server.actions.movement.DriveForward;
import com.github.TheDwoon.robots.server.actions.movement.TurnLeft;
import com.github.TheDwoon.robots.server.actions.movement.TurnRight;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBasicAI implements AI {

	private Robot robot;
	private Inventory inventory;
	private List<Field> fields;
	private List<LivingEntity> entitiesInRange;
	private List<Item> itemsInRange;

	private Field front;
	private Field left;
	private Field right;
	private Field back;
	private Field beneath;

	public AbstractBasicAI() {
		fields = new ArrayList<>();
		entitiesInRange = new ArrayList<>();
		itemsInRange = new ArrayList<>();
	}

	@Override
	public final void updateRobot(Robot robot) {
		this.robot = robot;
	}

	@Override
	public final void updateInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public final void updateVision(List<Field> fields) {
		this.fields = fields;
		entitiesInRange = new ArrayList<>();
		itemsInRange = new ArrayList<>();

		beneath = null;
		front = null;
		back = null;
		right = null;
		left = null;

		final int rx = robot.getX();
		final int ry = robot.getY();
		final Facing facing = robot.getFacing();
		fields.stream().forEach(field -> {
			final int dx = field.getX() - rx;
			final int dy = field.getY() - ry;
			if (dx == 0 && dy == 0) {
				beneath = field;
			} else if (checkFacing(dx, dy, facing)) {
				front = field;
			} else if (checkFacing(dx, dy, facing.opposite())) {
				back = field;
			} else if (checkFacing(dx, dy, facing.right())) {
				right = field;
			} else if (checkFacing(dx, dy, facing.left())) {
				left = field;
			} if (field.isOccupied()) {
				entitiesInRange.add(field.getOccupant());
			}
			if (field.hasItem()) {
				itemsInRange.add(field.getItem());
			}
		});
	}

	private boolean checkFacing(int dx, int dy, Facing facing) {
		return dx == facing.dx && dy == facing.dy;
	}

	protected final DriveForward driveForward() {
		return DriveForward.INSTANCE;
	}

	protected final DriveBackward driveBackward() {
		return DriveBackward.INSTANCE;
	}

	protected final TurnLeft turnLeft() {
		return TurnLeft.INSTANCE;
	}

	protected final TurnRight turnRight() {
		return TurnRight.INSTANCE;
	}

	protected final UseItem useItem(int slot) {
		return new UseItem(slot);
	}

	protected final PickUpItem pickUpItem() {
		return PickUpItem.INSTANCE;
	}

	protected final DropItem dropItem(int slot) {
		return new DropItem(slot);
	}

	protected final NoAction noAction() {
		return NoAction.INSTANCE;
	}

	protected final Field getFront() {
		return front;
	}

	protected final Field getLeft() {
		return left;
	}

	protected final Field getRight() {
		return right;
	}

	protected final Field getBack() {
		return back;
	}

	protected final Field getBeneath() {
		return beneath;
	}

	protected final Robot getRobot() {
		return robot;
	}

	protected final Inventory getInventory() {
		return inventory;
	}

	protected final List<Field> getFields() {
		return fields;
	}

	protected final List<LivingEntity> getEntitiesInRange() {
		return entitiesInRange;
	}

	protected final List<Item> getItemsInRange() {
		return itemsInRange;
	}
}
