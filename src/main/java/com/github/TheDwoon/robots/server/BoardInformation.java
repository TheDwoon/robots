package com.github.TheDwoon.robots.server;

import java.util.ArrayList;
import java.util.List;

import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Entity;

public class BoardInformation {
	private int positionX;
	private int positionY;
	private List<Field> fields;
	private List<Entity> entities;
	private Inventory inventory;
	
	public BoardInformation() {
		fields = new ArrayList<>(8);
		entities = new ArrayList<>(8);
	}
	
	public static BoardInformation obatinBoardInfoForEntity(Entity entity) {
		if (entity.getBoard() == null)
			return null;

		BoardInformation info = new BoardInformation();
		info.positionX = entity.getX();
		info.positionY = entity.getY();
		
		Board board = entity.getBoard();
		int x = entity.getX();
		int y = entity.getY();
		
		for (int i = x - 2; i <= x + 2; i++) {
			for (int j = y - 2; j <= y + 2; j++) {
				if (i < 0 || j < 0 || i >= board.getWidth() || j >= board.getHeight())
					continue;
				
				int dX = Math.abs(i - entity.getX());
				int dY = Math.abs(j - entity.getY());
				if (dX + dY <= 2)
					info.fields.add(board.getField(i, j));
			}
		}
		
		for (Entity e : board.getEntities()) {
			int dX = Math.abs(e.getX() - entity.getX());
			int dY = Math.abs(e.getY() - entity.getY());
			if (dX + dY <= 2) 
				info.entities.add(e);
		}
		
		return info;
	}
	
	public List<Field> getFields() {
		return fields;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
}
