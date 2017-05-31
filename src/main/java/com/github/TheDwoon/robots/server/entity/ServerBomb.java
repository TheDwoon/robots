package com.github.TheDwoon.robots.server.entity;

import com.github.TheDwoon.robots.game.items.Bomb;
import com.github.TheDwoon.robots.game.items.BombImpl;
import com.github.TheDwoon.robots.server.RobotsServer;

public class ServerBomb extends ServerItem implements Bomb {
	private final Bomb bomb;
	
	public ServerBomb(RobotsServer server) {
		this(server, new BombImpl());
	}
	
	public ServerBomb(RobotsServer server, Bomb bomb) {
		super(server, bomb);
		
		this.bomb = bomb;
	}

	public final Bomb getBomb() {
		return bomb;
	}
}
