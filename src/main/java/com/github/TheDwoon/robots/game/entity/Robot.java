package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.game.items.InventoryHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.abs;

public class Robot extends LivingEntity implements InventoryHolder {

	private static final Logger log = LogManager.getLogger();

	private static final int DEFAULT_MAX_HEALTH = 3;

	private String name;
	private int score;

	public Robot(String name) {
		this(-1, -1, name);
	}

	public Robot(int x, int y, String name) {
		super(x, y, DEFAULT_MAX_HEALTH);
		this.name = name;
		this.score = 0;
	}

	public Robot(long uuid, int x, int y, int maxHealth, int health, Facing facing, String name, int score) {
		super(uuid, x, y, maxHealth, health, facing);
		this.name = name;
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public void increaseScore(int score) {
		this.score += score;
		log.info("robot #{} has a new score of {} point{}", getUUID(), this.score, abs(this.score) == 1 ? "": 's');
	}

	public void decreaseScore(int score) {
		this.score -= score;
		log.info("robot #{} has a new score of {} point{}", getUUID(), this.score, abs(this.score) == 1 ? "": 's');
	}
}
