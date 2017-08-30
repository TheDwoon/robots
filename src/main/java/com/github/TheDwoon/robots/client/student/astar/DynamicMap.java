package com.github.TheDwoon.robots.client.student.astar;

import java.util.List;
import java.util.stream.IntStream;

import com.github.TheDwoon.robots.game.board.Field;
import com.github.TheDwoon.robots.game.board.Material;

public final class DynamicMap implements Map {
	private int width;
	private int height;
	private Field[][] map;
	
	public DynamicMap() {
		this(0, 0);
	}
	
	public DynamicMap(int width, int height) {
		this.width = width;
		this.height = height;
		this.map = new Field[width][height];
		
		IntStream.range(0, width).forEach(x -> IntStream.range(0, height).forEach(y -> map[x][y] = new Field(x, y, Material.GRASS)));
	}
	
	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public void updateFields(List<Field> fields) {
		final int maxX = fields.stream().mapToInt(Field::getX).max().orElse(0);
		final int maxY = fields.stream().mapToInt(Field::getY).max().orElse(0);
	
		if (width <= maxX || height <= maxY) {
			final int nextWidth = Math.max(width, maxX + 1);
			final int nextHeight = Math.max(height, maxY + 1);
			
			System.out.printf("Old: (%d x %d) ==> New: (%d x %d)\n", width, height, nextWidth, nextHeight);
			final Field[][] nextMap = new Field[nextWidth][nextHeight];
			for (int x = 0; x < nextWidth; x++) {
				for (int y = 0; y < nextHeight; y++) {
					if (x < width && y < height) {
						nextMap[x][y] = map[x][y];						
					} else {
						nextMap[x][y] = new Field(x, y, Material.GRASS);
					}
				}
			}
						
			map = nextMap;
			width = nextWidth;
			height = nextHeight;
		}		
		
		fields.forEach(field -> map[field.getX()][field.getY()] = field);
	}

	@Override
	public Field getField(int x, int y) {
		return map[x][y];
	}
	
	public void printMap() {
		System.out.printf("%d x %d\n", width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (map[x][y] == null) {
					System.out.println("WTF");
				}
				System.out.print(map[x][y].getMaterial().name().charAt(0) + " ");
			}
			
			System.out.println();
		}
	}
}
