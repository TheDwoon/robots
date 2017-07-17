package com.github.TheDwoon.robots.server.actions.movement;

import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.managers.AiManager;

public final class TurnRight implements PlayerAction {
	public static final TurnRight INSTANCE = new TurnRight();
	
	public TurnRight() {
		
	}

	@Override
	public void apply(AiManager aiManager) {
		aiManager.turnRight();
	}
}
