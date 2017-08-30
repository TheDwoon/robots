package com.github.TheDwoon.robots.client.student.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.actions.movement.DriveBackward;
import com.github.TheDwoon.robots.server.actions.movement.DriveForward;
import com.github.TheDwoon.robots.server.actions.movement.TurnLeft;
import com.github.TheDwoon.robots.server.actions.movement.TurnRight;

public final class AStar {
	public static HeuristicFunction heuristicFunction = (cx, cy, tx, ty) -> Math.sqrt((cx - tx) * (cx - tx) + (cy - ty) * (cy - ty));

	private AStar() {
		
	}
	
	public static List<PlayerAction> findPath(int startX, int startY, Facing initialFacing, int targetX, int targetY, Map map) {
		if (!map.isWithinMap(startX, startY))
			throw new IllegalArgumentException("start position not within map");
		
		// clamp target to be within the map
		final int tx = Math.max(0, Math.min(map.getWidth() - 1, targetX));
		final int ty = Math.max(0, Math.min(map.getHeight() - 1, targetY));		
		
		final boolean[][][] visited = new boolean[map.getWidth()][map.getHeight()][Facing.values().length];
		
		LinkedList<State> queue = new LinkedList<>();
		final State initialState = new State(null, startX, startY, initialFacing, null, 0, heuristicFunction.cost(startX, startY, tx, ty));
		queue.addFirst(initialState);
		
		// perform A*
		double bestHeuristic = initialState.heuristicCost;
		State bestState = initialState;
		while (!queue.isEmpty()) {
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
			
			queue.addAll(expandState(map, visited, state, tx, ty));		
		}
		
		// convert to actions
		LinkedList<PlayerAction> actions = new LinkedList<>();
		
		State state = bestState;
		while (state.previous != null) {
			actions.addFirst(state.performedAction);
			state = state.previous;
		}
		
		return actions;
	}	
	
	private static List<State> expandState(Map map, boolean[][][] visited, State state, int tx, int ty) {
		List<State> result = new ArrayList<>(4);
		
		int x, y;
		
		// drive forward
		x = state.x + state.facing.dx;
		y = state.y + state.facing.dy;
		if (map.isWithinMap(x, y) && !visited[x][y][state.facing.ordinal()] && map.getField(x, y).isVisitable()) {
			result.add(new State(state, x, y, state.facing, DriveForward.INSTANCE, state.walkingCost + 1, heuristicFunction.cost(x, y, tx, ty)));
			visited[x][y][state.facing.ordinal()] = true;			
		}
		
		// drive backward
		x = state.x - state.facing.dx;
		y = state.y - state.facing.dy;
		if (map.isWithinMap(x, y) && !visited[x][y][state.facing.opposite().ordinal()] && map.getField(x, y).isVisitable()) {
			result.add(new State(state, x, y, state.facing, DriveBackward.INSTANCE, state.walkingCost + 1, heuristicFunction.cost(x, y, tx, ty)));
			visited[x][y][state.facing.opposite().ordinal()] = true;
		}
		
		Facing nextFacing;
		x = state.x;
		y = state.y;
		// turn left
		nextFacing = state.facing.left();
		if (map.isWithinMap(x, y) && !visited[x][y][nextFacing.ordinal()] && map.getField(x, y).isVisitable()) {
			result.add(new State(state, x, y, nextFacing, TurnLeft.INSTANCE, state.walkingCost + 1, heuristicFunction.cost(x, y, tx, ty)));
			visited[x][y][nextFacing.ordinal()] = true;
		}
		
		// turn right
		nextFacing = state.facing.right();
		if (map.isWithinMap(x, y) && !visited[x][y][nextFacing.ordinal()] && map.getField(x, y).isVisitable()) {
			result.add(new State(state, x, y, nextFacing, TurnRight.INSTANCE, state.walkingCost + 1, heuristicFunction.cost(x, y, tx, ty)));
			visited[x][y][nextFacing.ordinal()] = true;
		}
		
		return result;
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

		@Override
		public int compareTo(State o) {
			return Double.compare(this.totalCost, o.totalCost);
		}
	}
}
