package com.github.TheDwoon.robots.server;

import com.github.TheDwoon.robots.server.managers.GameManager;

public interface GameLoader {
	GameManager loadGame(String mapName);
}
