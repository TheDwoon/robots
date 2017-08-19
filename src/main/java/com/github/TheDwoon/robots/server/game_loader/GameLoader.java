package com.github.TheDwoon.robots.server.game_loader;

import com.github.TheDwoon.robots.mapfile.MapFileParser;
import com.github.TheDwoon.robots.mapfile.ParseException;
import com.github.TheDwoon.robots.server.managers.BoardManager;
import com.github.TheDwoon.robots.server.managers.GameManager;

public interface GameLoader {
	GameManager loadGame(String mapName);

	static GameManager loadFromFile(String mapName) {
		BoardManager boardManager;
		try {
			boardManager = MapFileParser
					.parseBoard(GameLoader.class.getResourceAsStream("/map/" + mapName + ".map"));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		return new GameManager(boardManager);
	}
}
