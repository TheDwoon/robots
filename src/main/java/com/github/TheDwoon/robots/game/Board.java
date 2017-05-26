package com.github.TheDwoon.robots.game;

import java.util.ArrayList;
import java.util.List;

import com.github.TheDwoon.robots.game.entity.Entity;

public class Board {
	private static final int DEFAULT_WIDTH = 100;
	private static final int DEFAULT_HEIGHT = 100;
	private static final Material DEFAULT_MATERIAL = Material.GRASS;
	private static final Material DEFAULT_BORDER = Material.VOID;
		
	private final long uuid;
	private final List<Entity> entities;
	private final Field[][] fields;
		
	public Board(long uuid, final int width, final int height) {
		this.entities = new ArrayList<>(64);
		this.fields = new Field[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
					fields[x][y] = new Field(x, y, DEFAULT_BORDER);
				else
					fields[x][y] = new Field(x, y, DEFAULT_MATERIAL);
			}
		}
		this.uuid = uuid;
	}

	public Board(long uuid, final Field[][] fields) {
		this.entities = new ArrayList<>(64);
		this.fields = fields;
		this.uuid = uuid;
	}

	public Field getField(int x, int y) {
		return fields[x][y];
	}
	
	public void spawnEntity(Entity entity, int x, int y) {
		if (entity != null) {
			entity.setPosition(x, y);
			entities.add(entity);
		}
	}
	
	public void spawnEntity(Entity entity) {
		if (entity != null) {
			entities.add(entity);
		}
	}
	
	public void removeEntity(Entity entity) {
		if (entity != null)
			entities.remove(entity);
	}
		
	public final long getUUID() {
		return uuid;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Field[] row : fields) {
			for (int y = 0; y < row.length; y++) {
				sb.append(row[y]);
				if (y < row.length - 1) {
					sb.append(' ');
				}
			}
			sb.append('\n');
		}
		return sb.toString();
	}

}
