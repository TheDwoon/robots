package com.github.TheDwoon.robots.game;

public class Board {
	private static final int DEFAULT_WIDTH = 100;
	private static final int DEFAULT_HEIGHT = 100;

	private final Field[][] fields;

	public Board() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public Board(final int width, final int height) {
		this(new Field[width][height]);
	}

	public Board(final Field[][] fields) {
		this.fields = fields;
	}

	public void update(final Field... updates) {
		for (Field update : updates) {
			// TODO (sigmarw, 25.04.2017): maybe really update the field's members
			fields[update.getX()][update.getY()] = update;
		}
	}

}
