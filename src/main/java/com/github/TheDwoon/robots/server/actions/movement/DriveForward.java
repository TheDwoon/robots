package com.github.TheDwoon.robots.server.actions.movement;

import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.entity.ServerRobot;

public final class DriveForward implements PlayerAction {
	public static final DriveForward INSTANCE = new DriveForward();
	
	public DriveForward() {
		
	}

	@Override
	public void apply(ServerRobot robot) {
		robot.driveForward();
	}
}
