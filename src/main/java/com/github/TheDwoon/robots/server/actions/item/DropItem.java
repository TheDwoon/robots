package com.github.TheDwoon.robots.server.actions.item;

import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.managers.AiManager;

public final class DropItem implements PlayerAction {

	private final int slot;

	public DropItem(int slot) {
		this.slot = slot;
	}

	@Override
	public void apply(AiManager aiManager) {
		aiManager.dropItem(slot);
	}
}
