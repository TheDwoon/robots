package com.github.TheDwoon.robots.game;

import java.util.ArrayList;
import java.util.List;

import com.github.TheDwoon.robots.server.RobotsServer;
import com.github.TheDwoon.robots.server.entity.ServerEntity;
import com.github.TheDwoon.robots.server.entity.ServerRobot;

public class Board {
//	private static final int DEFAULT_WIDTH = 100;
//	private static final int DEFAULT_HEIGHT = 100;
	private static final Material DEFAULT_MATERIAL = Material.GRASS;
	private static final Material DEFAULT_BORDER = Material.VOID;
		
	private final RobotsServer server;
	private final long uuid;
	private final int width;
	private final int height;
	private final List<ServerEntity> entities;
	private final Field[][] fields;
		
	public Board(RobotsServer server, long uuid, final int width, final int height) {
		this.server = server;
		this.width = width;
		this.height = height;
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

	public Board(RobotsServer server, long uuid, final Field[][] fields) {
		this.server = server;
		this.entities = new ArrayList<>(64);
		this.fields = fields;
		this.width = fields.length;
		this.height = fields[0].length;
		this.uuid = uuid;
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Field getField(int x, int y) {
		return fields[x][y];
	}
	
	public void spawnEntity(ServerEntity entity, int x, int y) {
		if (entity != null) {
			entity.setPosition(x, y);
			spawnEntity(entity);
		}
	}
	
	public void spawnEntity(ServerEntity entity) {
		if (entity != null) {
			entities.add(entity);
						
			if (entity instanceof ServerRobot) {
				getServer().getEntityBroadcaster().spawnRobot(((ServerRobot) entity).getRobot());
			} else {
				getServer().getEntityBroadcaster().spawnEntity(entity.getEntity());
			}
		}		
	}
	
	public void removeEntity(ServerEntity entity) {
		if (entity != null) {
			entities.remove(entity);
			getServer().getEntityBroadcaster().removeEntity(entity.getUUID());
		}
	}
		
	public final long getUUID() {
		return uuid;
	}
	
	public List<ServerEntity> getEntities() {
		return entities;
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

	public final RobotsServer getServer() {
		return server;
	}
}
