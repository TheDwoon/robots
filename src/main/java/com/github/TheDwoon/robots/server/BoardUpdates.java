package com.github.TheDwoon.robots.server;

import java.util.ArrayList;
import java.util.List;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.entity.Entity;

public final class BoardUpdates {
	public final List<Entity> entitiesUpdates = new ArrayList<>(32);
	public final List<Field> fieldUpdates = new ArrayList<>(32);
	
	public BoardUpdates() {
		
	}
}
