package com.github.TheDwoon.robots.server;

import com.github.TheDwoon.robots.game.entity.Robot;

@FunctionalInterface
public interface ScoreCallback {

	void increaseScore(Robot receiver, int score);

	default void decreaseScore(Robot receiver, int score) {
		increaseScore(receiver, -score);
	}
}
