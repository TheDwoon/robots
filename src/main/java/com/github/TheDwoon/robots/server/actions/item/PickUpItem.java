package com.github.TheDwoon.robots.server.actions.item;

import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.managers.AiManager;

public final class PickUpItem implements PlayerAction {
	public static final PickUpItem INSTANCE = new PickUpItem();

	@Override
	public void apply(AiManager aiManager) {
		aiManager.pickUpItem();
	}
}
