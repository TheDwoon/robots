package com.github.TheDwoon.robots.server.actions;

import com.github.TheDwoon.robots.server.managers.AiManager;

public final class PickUpItem implements PlayerAction {
	public static final PickUpItem INSTANCE = new PickUpItem();

	@Override
	public void apply(AiManager aiManager) {
		aiManager.pickUpItem();
	}
}
