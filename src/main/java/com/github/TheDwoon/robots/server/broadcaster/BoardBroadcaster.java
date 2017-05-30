package com.github.TheDwoon.robots.server.broadcaster;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;

public final class BoardBroadcaster extends Broadcaster<BoardObserver> implements BoardObserver {

	public BoardBroadcaster() {
		super();
	}

	@Override
	public void setSize(long uuid, int width, int height) {
		notifyObservers(o -> o.setSize(uuid, width, height));
	}

	@Override
	public void updateField(long uuid, Field field) {
		notifyObservers(o -> o.updateField(uuid, field));
	}
}
