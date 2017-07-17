package com.github.TheDwoon.robots.server.actions;

import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.entity.ServerRobot;

public class UseItem implements PlayerAction {

	private Item item;

	public UseItem(Item item) {
		this.item = item;
	}

	@Override
	public void apply(ServerRobot robot) {
		// TODO (sigmarw, 17.07.2017): implement
	}
	
}
