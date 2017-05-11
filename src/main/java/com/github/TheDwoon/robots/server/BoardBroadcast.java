package com.github.TheDwoon.robots.server;

import java.util.ArrayList;
import java.util.List;

import com.github.TheDwoon.robots.game.BoardObserver;
import com.github.TheDwoon.robots.game.Field;

public final class BoardBroadcast implements BoardObserver {
	public static final BoardBroadcast INSTANCE = new BoardBroadcast();
	
	private final List<BoardObserver> observers;
	
	private BoardBroadcast() {
		this.observers = new ArrayList<>(32);
	}
	
	public void registerObserver(BoardObserver o) {
		if (o != null) {
			synchronized (observers) {
				observers.add(o);
			}
		}
	}
	
	public void removeObserver(BoardObserver o) {
		if (o != null) {
			synchronized (observers) {
				observers.remove(o);
			}
		}
	}

	@Override
	public void setSize(long uuid, int width, int height) {
		synchronized (observers) {
			observers.forEach(o -> o.setSize(uuid, width, height));
		}
	}

	@Override
	public void updateField(long uuid, Field field) {
		synchronized (observers) {
			observers.forEach(o -> o.updateField(uuid, field));
		}
	}
}
