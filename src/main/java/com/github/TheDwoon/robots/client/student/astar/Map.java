package com.github.TheDwoon.robots.client.student.astar;

import java.util.List;

import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.game.board.Field;
import com.github.TheDwoon.robots.game.board.Material;

public interface Map {
	int getXMin();
	int getXMax();
	int getYMin();
	int getYMax();
	int getWidth();
	int getHeight();
	
	void updateFields(List<Field> fields);
	
	default Field getField(int x, int y, Facing facing) {
		final int tx = x + facing.dx;
		final int ty = y + facing.dy;
		if (isWithinMap(tx, ty))
			return getField(tx, ty);
		else
			return new Field(tx, ty, Material.GRASS);
	}
		
	Field getField(int x, int y);
	boolean isExplored(int x, int y);
	
	default boolean isWithinMap(int x, int y) {
		return getXMin() <= x && x < getXMax() && getYMin() <= y && y < getYMax(); 
	}
}
