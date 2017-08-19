package com.github.TheDwoon.robots.server.game_loader;

import com.github.TheDwoon.robots.client.student.RandomDriveAI;
import com.github.TheDwoon.robots.client.student.RandomItemCollectorAI;
import com.github.TheDwoon.robots.client.student.RepeatingAI;
import com.github.TheDwoon.robots.game.items.Bomb;
import com.github.TheDwoon.robots.game.items.Gun;
import com.github.TheDwoon.robots.server.actions.NoAction;
import com.github.TheDwoon.robots.server.actions.movement.DriveForward;
import com.github.TheDwoon.robots.server.actions.movement.TurnLeft;
import com.github.TheDwoon.robots.server.actions.movement.TurnRight;
import com.github.TheDwoon.robots.server.managers.GameManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public class SimpleTestLoader implements GameLoader {

	private static final Logger log = LogManager.getLogger();

	@Override
	public GameManager loadGame(String mapName) {
		GameManager gameManager = GameLoader.loadFromFile(mapName);

		try {
			gameManager.spawnItems(Bomb.class, 6);
			gameManager.spawnItems(Gun.class, 7);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			log.catching(e);
		}

		gameManager.spawnAi(new RandomDriveAI());
		gameManager.spawnAi(new RandomItemCollectorAI());
		gameManager.spawnAi(new RepeatingAI(NoAction.INSTANCE));
		gameManager.spawnAi(new RepeatingAI(TurnLeft.INSTANCE));
		gameManager.spawnAi(new RepeatingAI(TurnRight.INSTANCE));
		gameManager.spawnAi(new RepeatingAI(DriveForward.INSTANCE, TurnRight.INSTANCE));
		gameManager.spawnAi(new RepeatingAI(DriveForward.INSTANCE));

		return gameManager;
	}
}
