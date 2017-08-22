package com.github.TheDwoon.robots.server;

import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.server.game_loader.GameLoader;
import com.github.TheDwoon.robots.server.game_loader.SimpleTestLoader;
import com.github.TheDwoon.robots.server.game_loader.WeaponTestLoader;
import com.github.TheDwoon.robots.server.managers.GameManager;

import java.io.IOException;

public final class RobotsServer implements Runnable {

	private GameManager gameManager;

	public RobotsServer(Level level) {
		gameManager = level.gameLoader.loadGame(level.mapName);
	}

	public static void main(final String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("server <levelname>");
			System.exit(0);
		}

		Level level = Level.valueOf(args[0].toUpperCase());
		if (level == null) {
			System.out.println("Unkown level");
			System.exit(0);
		}

		RobotsServer server = new RobotsServer(level);
		server.run();
		System.out.println("EXIT");
	}

	@Override
	public void run() {
		KryoNetLoggerProxy.setAsKryoLogger();

		try (AIServer aiServer = new AIServer(gameManager);
				UIServer uiServer = new UIServer(gameManager)) {
			Thread.sleep(5000);
			while (true) {
				// TODO (danielw, 30.05.2017): maybe a server "shell" here

				gameManager.makeTurn();

				Thread.sleep(500);
			}
		} catch (IOException | InterruptedException e) {
			// ignore
		}
	}

	public static enum Level {
		GRASS(GameLoader::loadFromFile, "grass"),
		SIMPLE_TEST(new SimpleTestLoader(), "simple"),
		WEAPON_TEST(new WeaponTestLoader(), "weapon_test"),
		BASIC_MOVEMENT(GameLoader::loadFromFile, "weapon_test"),
		OBSTACLE_COURSE(GameLoader::loadFromFile, "simple"),
		MAZE(GameLoader::loadFromFile, "maze"),
		MAZE_BIG(GameLoader::loadFromFile, "maze_big"),
		TASK_1(GameLoader::loadFromFile, "task1"),
		TASK_2(GameLoader::loadFromFile, "task2"),
		BATTLE_ROYAL(null, "simple");

		public final GameLoader gameLoader;
		public final String mapName;

		Level(GameLoader loader, String mapName) {
			this.gameLoader = loader;
			this.mapName = mapName;
		}
	}

	public GameManager getGameManager() {
		return gameManager;
	}
}
