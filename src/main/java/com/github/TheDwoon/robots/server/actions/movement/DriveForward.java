package com.github.TheDwoon.robots.server.actions.movement;

import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.managers.AiManager;

public final class DriveForward implements PlayerAction {
	public static final DriveForward INSTANCE = new DriveForward();

	@Override
	public void apply(AiManager aiManager) {
		aiManager.driveForward();
	}
}
