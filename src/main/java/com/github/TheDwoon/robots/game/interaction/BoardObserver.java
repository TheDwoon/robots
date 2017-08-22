package com.github.TheDwoon.robots.game.interaction;

import com.github.TheDwoon.robots.game.board.Field;

public interface BoardObserver {
	void setSize(long uuid, int width, int height);

	void updateFields(long uuid, Field[] fields);
}
