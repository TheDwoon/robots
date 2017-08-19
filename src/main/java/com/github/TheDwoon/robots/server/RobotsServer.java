package com.github.TheDwoon.robots.server;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.IntStream;

public final class RobotsServer implements Runnable {

	private static final Logger log = LogManager.getLogger();

	private GameManager gameManager;

	private RobotsServer() {
		loadSimpleGame();
	}

	private void loadSimpleGame() {
		gameManager = loadGame("simple");

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

		gameManager.spawnAi(new DuellingAI(Facing.EAST));
		gameManager.spawnAi(new DuellingAI(Facing.WEST));
	}

	private GameManager loadGame(final String mapName) {
		BoardManager boardManager = null;
		try {
			boardManager = MapFileParser
					.parseBoard(getClass().getResourceAsStream("/map/" + mapName + ".map"));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		return new GameManager(boardManager);
	}

	public static void main(final String[] args) throws IOException {
		RobotsServer server = new RobotsServer();
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
}
