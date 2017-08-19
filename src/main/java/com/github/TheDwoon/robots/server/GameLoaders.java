package com.github.TheDwoon.robots.server;

import com.github.TheDwoon.robots.mapfile.MapFileParser;
import com.github.TheDwoon.robots.mapfile.ParseException;
import com.github.TheDwoon.robots.server.managers.BoardManager;
import com.github.TheDwoon.robots.server.managers.GameManager;

public final class GameLoaders {
	public static GameManager loadDefault(String mapName) {
		BoardManager boardManager;
		try {
			boardManager = MapFileParser
					.parseBoard(GameLoaders.class.getResourceAsStream("/map/" + mapName + ".map"));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		return new GameManager(boardManager);
	}
}
