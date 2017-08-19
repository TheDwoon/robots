package com.github.TheDwoon.robots.server.game_loader;

import com.github.TheDwoon.robots.client.student.DuellingAI;
import com.github.TheDwoon.robots.game.Facing;
import com.github.TheDwoon.robots.server.managers.GameManager;

public class WeaponTestLoader implements GameLoader {

	@Override
	public GameManager loadGame(String mapName) {
		GameManager gameManager = GameLoader.loadFromFile(mapName);

		gameManager.spawnAi(new DuellingAI(Facing.EAST));
		gameManager.spawnAi(new DuellingAI(Facing.WEST));

		return gameManager;
	}
}
