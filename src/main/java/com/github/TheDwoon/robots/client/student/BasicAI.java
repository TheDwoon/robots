package com.github.TheDwoon.robots.client.student;

import com.github.TheDwoon.robots.server.actions.PlayerAction;

public class BasicAI extends AbstractBasicAI {

	@Override
	public PlayerAction makeTurn() {
		return driveForward();
	}

	@Override
	public String getRobotName() {
		return "BasicAI";
	}
}
