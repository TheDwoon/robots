package com.github.TheDwoon.robots.game.interaction;

import com.github.TheDwoon.robots.game.Field;

public interface BoardObserver {
	void setSize(long uuid, int width, int height);
	void updateField(long uuid, Field field);
}
