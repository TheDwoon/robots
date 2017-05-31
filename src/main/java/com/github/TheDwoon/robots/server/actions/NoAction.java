package com.github.TheDwoon.robots.server.actions;

import com.github.TheDwoon.robots.server.entity.ServerRobot;

public final class NoAction implements PlayerAction {
	public static final NoAction INSTANCE = new NoAction();
	
	public NoAction() {

	}
	
	@Override
	public void apply(ServerRobot robot) {
		
	}

}
