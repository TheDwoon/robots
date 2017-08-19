package com.github.TheDwoon.robots.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.TheDwoon.robots.client.student.DuellingAI;
import com.github.TheDwoon.robots.client.student.RandomDriveAI;
import com.github.TheDwoon.robots.client.student.RandomItemCollectorAI;
import com.github.TheDwoon.robots.client.student.RepeatingAI;
import com.github.TheDwoon.robots.game.Facing;
import com.github.TheDwoon.robots.game.items.Bomb;
import com.github.TheDwoon.robots.game.items.Gun;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.mapfile.MapFileParser;
import com.github.TheDwoon.robots.mapfile.ParseException;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.server.actions.NoAction;
import com.github.TheDwoon.robots.server.actions.movement.DriveForward;
import com.github.TheDwoon.robots.server.actions.movement.TurnLeft;
import com.github.TheDwoon.robots.server.actions.movement.TurnRight;
import com.github.TheDwoon.robots.server.managers.BoardManager;
import com.github.TheDwoon.robots.server.managers.GameManager;

public final class RobotsServer implements Runnable {

	private static final Logger log = LogManager.getLogger();

	private GameManager gameManager;

	private RobotsServer(Level level) {
		gameManager = level.gameLoader.loadGame(level.mapName);
//		populateMap();
	}

	public void populateMap() {
//		gameManager = loadGame("simple");

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
	}

	private void loadWeaponTest() {
		gameManager = loadGame("weapon_test");

		gameManager.spawnItemsPlaced(
				IntStream.rangeClosed(4, 10).mapToObj(i -> new Gun(i, 3)).toArray(Item[]::new));
		gameManager.spawnItemsPlaced(
				IntStream.rangeClosed(0, 2).mapToObj(i -> new Gun(i, 3)).toArray(Item[]::new));
		gameManager.spawnItemsPlaced(
				IntStream.rangeClosed(12, 14).mapToObj(i -> new Gun(i, 3)).toArray(Item[]::new));

		gameManager.spawnAi(new DuellingAI(Facing.EAST));
		gameManager.spawnAi(new DuellingAI(Facing.WEST));
	}

	private GameManager loadGame(final String mapName) {
		BoardManager boardManager;
		try {
			boardManager = MapFileParser
					.parseBoard(getClass().getResourceAsStream("/map/" + mapName + ".map"));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		return new GameManager(boardManager);
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

	@Override public void run() {
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
		GRASS(GameLoaders::loadDefault, "grass"),
		SIMPLE_TEST(GameLoaders::loadDefault, "simple"),
		WEAPON_TEST(GameLoaders::loadDefault, "weapon_test"),
		BASIC_MOVEMENT(GameLoaders::loadDefault, "simple"),
		OBSTACLE_COURSE(GameLoaders::loadDefault, "simple"),
		MAZE(GameLoaders::loadDefault, "simple"),
		BATTLE_ROYAL(null, "simple");

		public final GameLoader gameLoader;
		public final String mapName;

		private Level(GameLoader loader, String mapName) {
			this.gameLoader = loader;
			this.mapName = mapName;
		}
	}

	public GameManager getGameManager() {
		return gameManager;
	}
}
