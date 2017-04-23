package com.github.TheDwoon.robots.client;

import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.game.Field;

public final class RobotsClient {
	public static void main(final String[] args) {
		System.out.println("Client!");
	}

	private Board board;

	public RobotsClient() {
		board = new Board();
	}

	interface Client {
		void update(Field[] fields);
	}

}
