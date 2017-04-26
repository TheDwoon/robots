package com.github.TheDwoon.robots.game;

import java.util.ArrayList;
import java.util.List;

import com.github.TheDwoon.robots.game.entity.Entity;

public class Board {
	private static final int DEFAULT_WIDTH = 100;
	private static final int DEFAULT_HEIGHT = 100;
	private static final Material DEFAULT_MATERIAL = Material.GRASS;
	private static final Material DEFAULT_BORDER = Material.VOID;

	private final List<Entity> entities;
	private final Field[][] fields;
		
	public Board() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public Board(final int width, final int height) {
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

	}

	public Board(final Field[][] fields) {
		this.entities = new ArrayList<>(64);
		this.fields = fields;
	}

	public void update(final Field... updates) {
		for (Field update : updates) {
			// TODO (danielw, 26.04.2017): send updates to client
			fields[update.getX()][update.getY()] = update;
		}
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
	
	public void removeEntity(long uuid) {
		// TODO (danielw, 27.04.16): implement
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
