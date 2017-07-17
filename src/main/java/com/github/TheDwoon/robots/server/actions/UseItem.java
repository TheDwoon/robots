package com.github.TheDwoon.robots.server.actions;

import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.managers.AiManager;

public class UseItem implements PlayerAction {

	private Item item;

	public UseItem(Item item) {
		this.item = item;
	}

	@Override
	public void apply(AiManager aiManager) {
		aiManager.useItem(item);
	}
	
}
