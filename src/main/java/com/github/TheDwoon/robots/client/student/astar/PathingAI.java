package com.github.TheDwoon.robots.client.student.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.github.TheDwoon.robots.client.student.astar.AStar.Path;
import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.game.board.Field;
import com.github.TheDwoon.robots.game.board.Material;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.interaction.AI;
import com.github.TheDwoon.robots.game.items.Inventory;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.game.items.Star;
import com.github.TheDwoon.robots.server.actions.NoAction;
import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.actions.item.PickUpItem;
import com.github.TheDwoon.robots.server.actions.movement.DriveBackward;
import com.github.TheDwoon.robots.server.actions.movement.DriveForward;
import com.github.TheDwoon.robots.server.actions.movement.TurnLeft;

public final class PathingAI implements AI {
	private static final int ROBOT_VISION = 3;
	
	private final DynamicMap map;
	private final LinkedList<PlayerAction> actionQueue;	
	private final LinkedList<Waypoint> waypoints = new LinkedList<>();
	private final LinkedList<Waypoint> explorationPoints = new LinkedList<>();
	
	private Robot robot;
	@SuppressWarnings("unused")
	private Inventory inventory;
	private List<Item> itemsInRange;
	
	
	public PathingAI() {
		map = new DynamicMap();
		actionQueue = new LinkedList<>();
		itemsInRange = new LinkedList<>();
	}
	
	@Override
	public PlayerAction makeTurn() {		
		searchStars();

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
		final int x = robot.getX();
		final int y = robot.getY();
		final Facing facing = robot.getFacing();
		
		if (explorationPoints.isEmpty()) 
			exploreWorld();
		
		// remove reached landmarks
		removeReachedWaypoints(waypoints);
		removeReachedWaypoints(explorationPoints);
		
		List<Path> paths = new LinkedList<>();		
		waypoints.stream().map(w -> AStar.findPath(x, y, facing, w.x, w.y, map)).filter(path -> !path.actions.isEmpty()).forEach(path -> {
			path.actions.add(PickUpItem.INSTANCE);
			paths.add(path);
		});
		
		if (paths.isEmpty()) {
			System.out.println("EXPLORE!");
			Iterator<Waypoint> it = explorationPoints.iterator();
			while (it.hasNext()) {
				Waypoint w = it.next();
				if (map.isWithinMap(w.x, w.y) && !map.getField(w.x, w.y).isVisitable()) {
					it.remove();
					continue;
				}
			}
			
			List<Waypoint> removeExplore = new LinkedList<>();
			explorationPoints.parallelStream().forEach(w -> { 
				Path p = AStar.findPath(x, y, facing, w.x, w.y, map);
				if (!p.reached || p.actions.isEmpty()) 
					removeExplore.add(w);
				
				paths.add(p);
			});
			
			explorationPoints.removeAll(removeExplore);
		}
		
		// sort shortest path to be first
		Collections.sort(paths, (a, b) -> Integer.compare(a.actions.size(), b.actions.size()));			
		
		
		Optional<Path> path = paths.stream().findFirst();
		
		if (path.isPresent()) {
			actionQueue.addAll(path.get().actions);			
		} else {
			actionQueue.add(TurnLeft.INSTANCE);
		}
	}
	
	private void searchStars() {
		itemsInRange.stream().filter(item -> item instanceof Star).map(item -> new Waypoint(item.getX(), item.getY()))
			.filter(w -> !waypoints.contains(w)).forEach(waypoints::add);;
	}
	
	private void exploreWorld() {		
		final double acceptedDistance = 2;					
		
		for (int x = map.getXMin(); x < map.getXMax() + 3; x += 3) {
			for (int y = map.getYMin(); y < map.getYMax() + 3; y += 3) {
				if (!map.isExplored(x, y))
					explorationPoints.add(new Waypoint(x, y, acceptedDistance));
			}
		}
	}
	
	private void removeReachedWaypoints(List<Waypoint> waypoints) {
		final int x = robot.getX();
		final int y = robot.getY();
		
		Iterator<Waypoint> it = waypoints.iterator();
		while (it.hasNext()) {
			final Waypoint waypoint = it.next();
			if (waypoint.isReached(x, y))
				it.remove();
		}
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
		final int x = robot.getX();
		final int y = robot.getY();
		List<Field> expandedVision = new ArrayList<>((int) Math.pow(ROBOT_VISION * 2 + 1, 2));
		
		Field[][] visionMap = new Field[ROBOT_VISION * 2 + 1][ROBOT_VISION * 2 + 1]; 
		fields.forEach(f -> visionMap[f.getX() - x + ROBOT_VISION][f.getY() - y + ROBOT_VISION] = f);
		IntStream.range(0, 2 * ROBOT_VISION + 1).forEach(cx -> IntStream.range(0, 2 * ROBOT_VISION + 1).forEach(cy -> {
			if (visionMap[cx][cy] == null)
				visionMap[cx][cy] = new Field(cx + x - ROBOT_VISION, cy + y - ROBOT_VISION, Material.VOID);
			
			expandedVision.add(visionMap[cx][cy]);
		}));
		
		
		map.updateFields(expandedVision);
		
		itemsInRange.clear();
		fields.stream().filter(Field::hasItem).map(Field::getItem).forEach(itemsInRange::add);
	}

	public static final class Waypoint {
		public final int x;
		public final int y;
		public final double acceptedDistance;		
		
		public Waypoint(int x, int y) {
			this(x, y, 0);
		}
		
		public Waypoint(int x, int y, double acceptedDistance) {
			this.x = x;
			this.y = y;
			this.acceptedDistance = acceptedDistance;
		}
		
		public boolean isReached(int x, int y) {
			final int dx = x - this.x;
			final int dy = y - this.y;
			
			return Math.sqrt(dx * dx + dy * dy) <= acceptedDistance;					
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;

			Waypoint other = (Waypoint) obj;
			
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			
			return true;
		}
	}
}
