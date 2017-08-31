package com.github.TheDwoon.robots.client.student.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.game.board.Field;
import com.github.TheDwoon.robots.game.board.Material;
import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.actions.movement.DriveBackward;
import com.github.TheDwoon.robots.server.actions.movement.DriveForward;
import com.github.TheDwoon.robots.server.actions.movement.TurnLeft;
import com.github.TheDwoon.robots.server.actions.movement.TurnRight;

public final class AStar {
	private static final int MAX_ITERATIONS = 10000;
	
	public static HeuristicFunction heuristicFunction = (cx, cy, tx, ty) -> (cx - tx) * (cx - tx) + (cy - ty) * (cy - ty);

	private AStar() {
		
	}
	
	public static Path findPath(int startX, int startY, Facing initialFacing, int targetX, int targetY, Map map) {
		if (!map.isWithinMap(startX, startY))
			throw new IllegalArgumentException(String.format("start position not within map: (%d, %d)", startX, startY));
		
		// clamp target to be within the map
//		final int tx = Math.max(0, Math.min(map.getWidth() - 1, targetX));
//		final int ty = Math.max(0, Math.min(map.getHeight() - 1, targetY));
		final int tx = targetX;
		final int ty = targetY;
		
//		final boolean[][][] visited = new boolean[map.getWidth()][map.getHeight()][Facing.values().length];
		
		LinkedList<State> queue = new LinkedList<>();
		final State rootState = new State(null, startX, startY, initialFacing, null, 0, heuristicFunction.cost(startX, startY, tx, ty));
		queue.addFirst(rootState);
		
		// perform A*
		int iteration = 0;
		double bestHeuristic = rootState.heuristicCost;
		State bestState = rootState;
		while (!queue.isEmpty() && iteration < MAX_ITERATIONS) {
			iteration++;
			Collections.sort(queue);
			State state = queue.removeFirst();
			
			if (state.x == tx && state.y == ty) {
				bestHeuristic = state.heuristicCost;
				bestState = state;
				break;
			} else if (state.heuristicCost < bestHeuristic) {
				bestHeuristic = state.heuristicCost;
				bestState = state;
			}
			
			queue.addAll(expandState(map, rootState, state, tx, ty));		
		}
		
		// convert to actions
		LinkedList<PlayerAction> actions = new LinkedList<>();
		
		State state = bestState;
		while (state.previous != null) {
			actions.addFirst(state.performedAction);
			state = state.previous;
		}
		
		Collections.sort(queue);
		System.out.printf("Iterations: %d, Queue: %d, nextState: (%d, %d, %s, %.2f), BestState: (%d, %d, %s, %.2f), Target: (%d, %d) Path: %d\n",
				iteration, queue.size(),
				queue.isEmpty() ? 0 : queue.getFirst().x, queue.isEmpty() ? 0 : queue.getFirst().y, queue.isEmpty() ? "none" : queue.getFirst().facing.name(), queue.isEmpty() ? 0 : queue.getFirst().totalCost,
						bestState.x, bestState.y, bestState.facing.name(), bestState.totalCost,
						tx, ty, actions.size());
		
		return new Path(actions, bestState.x == tx && bestState.y == ty);
	}	
	
	private static List<State> expandState(Map map, State rootState, State state, int tx, int ty) {
		List<State> result = new ArrayList<>(4);
		
		int x, y;
		
		// drive forward
		x = state.x + state.facing.dx;
		y = state.y + state.facing.dy;
		if (getFieldOrGrass(map, x, y).isVisitable()) {
			State s = new State(state, x, y, state.facing, DriveForward.INSTANCE, state.walkingCost + 1, heuristicFunction.cost(x, y, tx, ty));
			if (!rootState.contains(s))
				result.add(s);
		}
		
		// drive backward
		x = state.x - state.facing.dx;
		y = state.y - state.facing.dy;
		if (getFieldOrGrass(map, x, y).isVisitable()) {
			State s = new State(state, x, y, state.facing, DriveBackward.INSTANCE, state.walkingCost + 1, heuristicFunction.cost(x, y, tx, ty));
			if (!rootState.contains(s))
				result.add(s);
		}
		
		Facing nextFacing;
		x = state.x;
		y = state.y;
		// turn left
		nextFacing = state.facing.left();
		if (getFieldOrGrass(map, x, y).isVisitable()) {
			State s = new State(state, x, y, nextFacing, TurnLeft.INSTANCE, state.walkingCost + 1, heuristicFunction.cost(x, y, tx, ty));
			if (!rootState.contains(s))
				result.add(s);
		}
		
		// turn right
		nextFacing = state.facing.right();
		if (getFieldOrGrass(map, x, y).isVisitable()) {
			State s = new State(state, x, y, nextFacing, TurnRight.INSTANCE, state.walkingCost + 1, heuristicFunction.cost(x, y, tx, ty));
			if (!rootState.contains(s))
				result.add(s);
		}
		
		state.children.addAll(result);
		return result;
	}
		
	private static Field getFieldOrGrass(Map map, int x, int y) {
		Field field = null;
		if (map.isWithinMap(x, y))
			field = map.getField(x, y);
		
		if (field != null)
			return field;
		else
			return new Field(x, y, Material.GRASS);
	}
	
	public static interface HeuristicFunction {
		double cost(int cx, int cy, int tx, int ty);
	}
	
	private static class State implements Comparable<State> {
		private final State previous;
		private final int x;
		private final int y;
		private final Facing facing;		
		private final int walkingCost;
		private final double heuristicCost;
		private final double totalCost;
		private final PlayerAction performedAction;
		private final List<State> children = new ArrayList<>(4);
		
		private State(State previous, int x, int y, Facing facing, PlayerAction performedAction, int walkingCost, double heuristicCost) {
			this.previous = previous;
			this.x = x;
			this.y = y;
			this.facing = facing;
			this.performedAction = performedAction;
			this.walkingCost = walkingCost;
			this.heuristicCost = heuristicCost;
			this.totalCost = walkingCost + heuristicCost;
		}
		
		public boolean contains(State other) {
			LinkedList<State> states = new LinkedList<>();
			states.add(this);
			
			while (!states.isEmpty()) {
				State state = states.removeFirst();
				if (state.equals(other))
					return true;
				
				states.addAll(0, state.children);
			}

			return false;
		}

		@Override
		public int compareTo(State o) {
			return Double.compare(this.totalCost, o.totalCost);
		}
		
		private boolean equals(State o) {
			return x == o.x && y == o.y && facing == o.facing;
		}
	}
	
	public static class Path {
		public final List<PlayerAction> actions;
		public final boolean reached;
		
		public Path(List<PlayerAction> actions, boolean reached) {
			this.actions = actions;
			this.reached = reached;
		}
	}
}
