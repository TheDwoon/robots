package com.github.TheDwoon.robots.client.student.astar;

import java.util.List;
import java.util.stream.IntStream;

import com.github.TheDwoon.robots.game.board.Field;
import com.github.TheDwoon.robots.game.board.Material;

public final class DynamicMap implements Map {
	private int xMin;
	private int xMax;
	private int yMin;
	private int yMax;
	private Field[][] map;
	private boolean[][] explored;
	
	public DynamicMap() {
		this(0, 0);
	}	
	
	public DynamicMap(int width, int height) {
		this(0, width, 0, height);
	}
	
	public DynamicMap(int xMin, int xMax, int yMin, int yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.map = new Field[getWidth()][getHeight()];
		this.explored = new boolean[getWidth()][getHeight()];
		
		IntStream.range(xMin, xMax).forEach(x -> IntStream.range(yMin, yMax).forEach(y -> map[x - xMin][y - yMin] = new Field(x, y, Material.GRASS)));
	}
	
	@Override
	public void updateFields(List<Field> fields) {	
		final int minX = fields.stream().mapToInt(Field::getX).min().orElse(0);
		final int maxX = fields.stream().mapToInt(Field::getX).max().orElse(-1) + 1;
		final int minY = fields.stream().mapToInt(Field::getY).min().orElse(0);
		final int maxY = fields.stream().mapToInt(Field::getY).max().orElse(-1) + 1;
	
		// resize map if needed 
		if (minX < this.xMin || this.xMax < maxX || minY < this.yMin || this.yMax < maxY) {			
			final int nextXMin = Math.min(this.xMin, minX);
			final int nextXMax = Math.max(this.xMax, maxX);
			final int nextYMin = Math.min(this.yMin, minY);
			final int nextYMax = Math.max(this.xMax, maxY);
			
			final int nextWidth = nextXMax - nextXMin;
			final int nextHeight = nextYMax - nextYMin;
			
			System.out.printf("Old: [%d, %d, %d, %d](%d x %d) ==> New: [%d, %d, %d, %d](%d x %d)\n",
					xMin, xMax, yMin, yMax, getWidth(), getHeight(), 
					nextXMin, nextXMax, nextYMin, nextYMax, nextWidth, nextHeight);
			final Field[][] nextMap = new Field[nextWidth][nextHeight];
			final boolean[][] nextExplored = new boolean[nextWidth][nextHeight];
			for (int x = nextXMin; x < nextXMax; x++) {
				for (int y = nextYMin; y < nextYMax; y++) {
					if (this.xMin <= x && x < this.xMax && this.yMin <= y && y < this.yMax) {
						nextMap[x - nextXMin][y - nextYMin] = map[x - this.xMin][y - this.yMin];		
						nextExplored[x - nextXMin][y - nextYMin] = explored[x - this.xMin][y - this.yMin]; 
					} else {
						nextMap[x - nextXMin][y - nextYMin] = new Field(x, y, Material.GRASS);
					}
				}
			}
			
			map = nextMap;
			explored = nextExplored;
			xMin = nextXMin;
			xMax = nextXMax;
			yMin = nextYMin;
			yMax = nextYMax;
		}		
		
		fields.forEach(field -> { 
			map[field.getX() - xMin][field.getY() - yMin] = field;
			explored[field.getX() - xMin][field.getY() - yMin] = true;
		});
	}

	@Override
	public Field getField(int x, int y) {
		return map[x - xMin][y - yMin];
	}
		
	@Override
	public boolean isExplored(int x, int y) {
		if (isWithinMap(x, y))
			return explored[x - xMin][y - yMin];
		else
			return false;
	}

	public void printMap() {
		System.out.printf("%d x %d\n", getWidth(), getHeight());
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				if (map[x][y] == null) {
					System.out.println("WTF");
				}
				System.out.print(map[x][y].getMaterial().name().charAt(0) + " ");
			}
			
			System.out.println();
		}
	}

	@Override
	public int getXMin() {
		return xMin;
	}

	@Override
	public int getXMax() {
		return xMax;
	}

	@Override
	public int getYMin() {
		return yMin;
	}

	@Override
	public int getYMax() {
		return yMax;
	}

	@Override
	public int getWidth() {
		return xMax - xMin;
	}

	@Override
	public int getHeight() {
		return yMax - yMin;
	}
}
