package com.github.TheDwoon.robots.server.actions.movement;

import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.managers.AiManager;

public final class DriveBackward implements PlayerAction {
	public static final DriveBackward INSTANCE = new DriveBackward();

	@Override
	public void apply(AiManager aiManager) {
		aiManager.driveBackward();
	}
}
