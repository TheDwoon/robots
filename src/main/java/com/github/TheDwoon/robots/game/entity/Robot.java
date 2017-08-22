package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Facing;
import com.github.TheDwoon.robots.game.InventoryHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.Math.abs;

public class Robot extends LivingEntity implements InventoryHolder {

	private static final Logger log = LogManager.getLogger();

	private static final int DEFAULT_MAX_HEALTH = 3;

	private int score;

	public Robot() {
		this(-1, -1);
		this.score = 0;
	}

	public Robot(int x, int y) {
		super(x, y, DEFAULT_MAX_HEALTH);
		this.score = 0;
	}

	public Robot(long uuid, int x, int y, int maxHealth, int health, Facing facing, int score) {
		super(uuid, x, y, maxHealth, health, facing);
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void addScore(int score) {
		this.score += score;
		log.info("robot #{} has a new score of {} point{}", getUUID(), this.score, abs(this.score) == 1 ? "": 's');
	}

	public void subtractScore(int score) {
		this.score -= score;
		log.info("robot #{} has a new score of {} point{}", getUUID(), this.score, abs(this.score) == 1 ? "": 's');
	}
}
