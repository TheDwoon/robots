package com.github.TheDwoon.robots.server.actions.movement;

import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.entity.ServerRobot;

public final class DriveBackward implements PlayerAction {
	public static final DriveBackward INSTANCE = new DriveBackward();
	
	public DriveBackward() {
		
	}

	@Override
	public void apply(ServerRobot robot) {
		robot.driveBackward();
	}
}
