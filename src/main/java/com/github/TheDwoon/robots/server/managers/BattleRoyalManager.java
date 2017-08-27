package com.github.TheDwoon.robots.server.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.github.TheDwoon.robots.client.student.BasicAI;
import com.github.TheDwoon.robots.client.student.DuellingAI;
import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.game.board.Field;
import com.github.TheDwoon.robots.game.board.Material;
import com.github.TheDwoon.robots.game.items.Bomb;

public final class BattleRoyalManager extends GameManager {
	private static final int MIN_RADIUS = 2;
	private static final int INIT_ROUNDS_LEFT = 100;
	private static final int ROUNDS_PER_CYCLE = 50;	
	
	private final List<Field> fieldsToTurn = new ArrayList<>();
	
	private int cycle = 0;
	
	private int centerX;
	private int centerY;
	private int radius;
	
	private int roundsLeft;
		
	public BattleRoyalManager(BoardManager boardManager) {
		super(boardManager, false);
		
		radius = Math.max(boardManager.getWidth(), boardManager.getHeight());
		centerX = (int) (Math.random() * boardManager.getWidth());
		centerY = (int) (Math.random() * boardManager.getHeight());		
		roundsLeft = INIT_ROUNDS_LEFT;	
		
		for (int i = 0; i < 10; i++)
			spawnAi(new DuellingAI(Facing.SOUTH));
	}

	@Override
	public void makeTurn() {
		if (roundsLeft <= 0) {
			relocateCircle();
			cycle++;
			roundsLeft = ROUNDS_PER_CYCLE;
		} else {
			roundsLeft--;
		}
		
		for (int i = 0; i < boardManager.getWidth() * boardManager.getHeight() / (1.25 * ROUNDS_PER_CYCLE) && !fieldsToTurn.isEmpty(); i++) {
			Field field = fieldsToTurn.remove(0);
			boardManager.setMaterial(field.getX(), field.getY(), Material.SCORCHED_EARTH);
		}
		
		synchronized (robots) {
			// damage robots outside the circle
			robots.keySet().stream().filter(robot -> robot.isAlive())
				.filter(robot -> boardManager.getField(robot.getX(), robot.getY()).getMaterial() == Material.SCORCHED_EARTH)
				.forEach(robot -> boardManager.damageLivingEntity(robot.getX(), robot.getY(), 1, null));
			
			if (cycle > 0 && robots.keySet().stream().filter(robot -> robot.isAlive()).count() == 1) {
				System.out.println(robots.keySet().iterator().next().getName() + " won the game!");
				cycle = -10000;
			}
		}

		super.makeTurn();			
	}
	
	private void relocateCircle() {
		if (radius <= MIN_RADIUS)
			return;
		
		final int xMin = Math.max(0, centerX - radius / 2), xMax = Math.min(boardManager.getWidth(), centerX + radius / 2);
		final int yMin = Math.max(0, centerY - radius / 2), yMax = Math.min(boardManager.getHeight(), centerY + radius / 2);
		
		radius = Math.max(MIN_RADIUS, radius / 2);
		centerX = (int) (Math.random() * (xMax - xMin)) + xMin;
		centerY = (int) (Math.random() * (yMax - yMin)) + yMin;
		
		boardManager.spawnItem(new Bomb(), centerX, centerY);

		final int width = boardManager.getWidth();
		final int height = boardManager.getHeight();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final int dx = x - centerX;
				final int dy = y - centerY;
				final Field field = boardManager.getField(x, y);
				if (Math.sqrt(dx * dx + dy * dy) > radius && field.getMaterial() != Material.SCORCHED_EARTH) {
					fieldsToTurn.add(field);
				}
			}
		}
				
		Collections.sort(fieldsToTurn, (f1, f2) -> -Double.compare(distanceToCenter(f1), distanceToCenter(f2)));
	}
	
	private double distanceToCenter(Field f) {
		final int dx = f.getX() - centerX;
		final int dy = f.getY() - centerY;
		return Math.sqrt(dx * dx + dy * dy);
	}
}
