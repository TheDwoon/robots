package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.interaction.AiObserver;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.server.AIServer;
import com.github.TheDwoon.robots.server.RobotsServer;
import com.github.TheDwoon.robots.server.managers.GameManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RobotsCombinedServerView extends Application {

	public static void main(final String[] args) {
		launch(args);
	}

	private Thread robotsServerThread;

	@Override
	public void start(final Stage primaryStage) throws Exception {
		RobotsServer robotsServer = new RobotsServer(RobotsServer.Level.WEAPON_TEST);
		GameManager gameManager = robotsServer.getGameManager();
		robotsServerThread = new Thread(() -> runServer(gameManager), "robotsServer");
		robotsServerThread.start();

		GameDisplay gameDisplay = new GameDisplay();
		Object gameDisplayFxThreadAdapter = FxThreadAdapter
				.create(gameDisplay, BoardObserver.class, AiObserver.class,
						InventoryObserver.class);

		gameManager.addObserver((BoardObserver) gameDisplayFxThreadAdapter);
		gameManager.addObserver((AiObserver) gameDisplayFxThreadAdapter);
		gameManager.addObserver((InventoryObserver) gameDisplayFxThreadAdapter);

		primaryStage.setScene(new Scene(gameDisplay));
		primaryStage.setTitle("RobotsUI");
		// primaryStage.setFullScreen(true);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		robotsServerThread.interrupt();
	}

	public void runServer(GameManager gameManager) {
		KryoNetLoggerProxy.setAsKryoLogger();

		try (AIServer aiServer = new AIServer(gameManager)) {
			Thread.sleep(500);
			System.out.println("Game started");
			for (int round = 1; !Thread.interrupted(); round++) {
				System.out.printf("Round %d%n", round);

				gameManager.makeTurn();

				Thread.sleep(500);
			}
		} catch (IOException | InterruptedException e) {
			// ignore
		}
	}
}
