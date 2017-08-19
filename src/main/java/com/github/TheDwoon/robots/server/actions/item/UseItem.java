package com.github.TheDwoon.robots.server.actions.item;

import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.managers.AiManager;

public class UseItem implements PlayerAction {

	private int slot;

	public UseItem(int slot) {
		this.slot = slot;
	}

	@Override
	public void apply(AiManager aiManager) {
		aiManager.useItem(slot);
	}

}
