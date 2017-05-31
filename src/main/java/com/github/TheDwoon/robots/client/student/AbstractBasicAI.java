package com.github.TheDwoon.robots.client.student;

import java.util.ArrayList;
import java.util.List;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.AI;
import com.github.TheDwoon.robots.server.actions.PlayerAction;

public class AbstractBasicAI implements AI {
	private Robot robot;
	private List<Field> fields;
	private List<Entity> entities;
	
	public AbstractBasicAI() {
		fields = new ArrayList<>();
		entities = new ArrayList<>();
	}
	
	@Override
	public final void updateVision(List<Field> fields, List<Entity> entities) {
		this.fields = fields;
		this.entities = entities;
	}

	public final List<Field> getFields() {
		return fields;
	}
	
	public final List<Entity> getEntities() {
		return entities;
	}

	@Override
	public PlayerAction makeTurn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateRobot(Robot entity) {
		// TODO Auto-generated method stub
		
	}
}
