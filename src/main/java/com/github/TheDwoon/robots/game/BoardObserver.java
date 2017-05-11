package com.github.TheDwoon.robots.game;

public interface BoardObserver {
	void setSize(long uuid, int width, int height);
	void updateField(long uuid, Field field);
}
