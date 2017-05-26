package com.github.TheDwoon.robots.server.manager;

import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.server.RobotsServer;
import com.github.TheDwoon.robots.server.UUIDGenerator;

public class BoardManager {
	private final RobotsServer server;
	private final Board board;
	
	public BoardManager(RobotsServer server, int width, int height) {
		this.server = server;
		this.board = new Board(UUIDGenerator.obtainUUID(), width, height);
		// TODO remote stuff.
	}
		
	public void setField(Board board, Field field) {
		
	}
}
