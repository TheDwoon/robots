package com.github.TheDwoon.robots.server;

import java.util.List;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.server.actions.PlayerAction;

public interface AI {
	void updateVision(List<Field> fields, List<Entity> entities);
	PlayerAction makeTurn();
}
