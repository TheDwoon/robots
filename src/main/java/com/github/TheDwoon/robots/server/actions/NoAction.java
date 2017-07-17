package com.github.TheDwoon.robots.server.actions;

import com.github.TheDwoon.robots.server.managers.AiManager;

public final class NoAction implements PlayerAction {
	public static final NoAction INSTANCE = new NoAction();

	@Override
	public void apply(AiManager aiManager) {
		// do nothing.
	}
}
