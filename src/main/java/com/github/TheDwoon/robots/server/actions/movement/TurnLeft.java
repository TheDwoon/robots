package com.github.TheDwoon.robots.server.actions.movement;

import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.managers.AiManager;

public final class TurnLeft implements PlayerAction {
	public static final TurnLeft INSTANCE = new TurnLeft();			
	
	public TurnLeft() {
		
	}

	@Override
	public void apply(AiManager aiManager) {
		aiManager.turnLeft();
	}
}
