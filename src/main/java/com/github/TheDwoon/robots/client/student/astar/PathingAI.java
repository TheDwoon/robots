package com.github.TheDwoon.robots.client.student.astar;

import java.util.LinkedList;
import java.util.List;

import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.game.board.Field;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.interaction.AI;
import com.github.TheDwoon.robots.game.items.Inventory;
import com.github.TheDwoon.robots.server.actions.NoAction;
import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.actions.item.PickUpItem;
import com.github.TheDwoon.robots.server.actions.movement.DriveBackward;
import com.github.TheDwoon.robots.server.actions.movement.DriveForward;

public final class PathingAI implements AI {
	private final DynamicMap map;
	private final LinkedList<PlayerAction> actionQueue;
	private final LinkedList<Location> locations = new LinkedList<>();
	
	private Robot robot;
	@SuppressWarnings("unused")
	private Inventory inventory;
	
	
	
	public PathingAI() {
		map = new DynamicMap(60, 40);
		actionQueue = new LinkedList<>();
		
		locations.add(new Location(22, 16));
		locations.add(new Location(4, 4));
		locations.add(new Location(42, 25));
		locations.add(new Location(4, 24));
		locations.add(new Location(41, 5));		
	}
	
	@Override
	public PlayerAction makeTurn() {
		final int x = robot.getX();
		final int y = robot.getY();
		final Facing facing = robot.getFacing();
		
		System.out.println("ActionQueue: " + actionQueue.size());
		
		if (actionQueue.isEmpty())
			planMovement();
		
		if (actionQueue.isEmpty()) {
			return NoAction.INSTANCE;
		}
		
		PlayerAction action = actionQueue.removeFirst();
		// check if next move is blocked
		// TODO this is useful. But not when we only make dumb moves
		if (action == DriveForward.INSTANCE 
				&& (map.getField(x, y, facing).isOccupied() || !map.getField(x, y, facing).isVisitable())
				|| action == DriveBackward.INSTANCE 
				&& (map.getField(x, y, facing.opposite()).isOccupied() || !map.getField(x, y, facing.opposite()).isVisitable())) {
			actionQueue.clear();
			return NoAction.INSTANCE;
		}
		
		return action;
	}

	private void planMovement() {
		if (locations.isEmpty())
			return;
		
		Location location = locations.getFirst();
		while (location.x == robot.getX() && location.y == robot.getY()) {
			locations.removeFirst();
			if (locations.isEmpty())
				return;
			
			location = locations.getFirst();			
		}
		
		System.out.printf("Heading for (%d, %d)!\n", location.x, location.y);
		
		actionQueue.addAll(AStar.findPath(robot.getX(), robot.getY(), robot.getFacing(), location.x, location.y, map));
		actionQueue.forEach(a -> System.out.print(a.getClass().getSimpleName() + ", "));
		if (actionQueue.size() > 0)
			System.out.println();
		
		actionQueue.addLast(PickUpItem.INSTANCE);
	}
	
	@Override
	public String getRobotName() {
		return "Get Schwifty";
	}

	@Override
	public void updateRobot(Robot robot) {
		this.robot = robot;
	}

	@Override
	public void updateInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public void updateVision(List<Field> fields) {
		map.updateFields(fields);
	}

	public static class Location {
		public final int x;
		public final int y;
		
		public Location(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
