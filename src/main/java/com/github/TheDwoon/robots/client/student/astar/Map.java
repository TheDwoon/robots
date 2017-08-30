package com.github.TheDwoon.robots.client.student.astar;

import java.util.List;

import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.game.board.Field;

public interface Map {
	int getWidth();
	int getHeight();
	void updateFields(List<Field> fields);
	
	default Field getField(int x, int y, Facing facing) {
		return getField(x + facing.dx, y + facing.dy);
	}
	
	Field getField(int x, int y);
	
	default boolean isWithinMap(int x, int y) {
		return 0 <= x && x < getWidth() && 0 <= y && y < getHeight(); 
	}
}
