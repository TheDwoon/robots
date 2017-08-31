package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.interaction.AiObserver;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.server.AIServer;
import com.github.TheDwoon.robots.server.RobotsServer;
import com.github.TheDwoon.robots.server.managers.GameManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class RobotsCombinedServerView extends Application {

	private static final Logger log = LogManager.getLogger();

	public static void main(final String[] args) {
		launch(args);
	}

	private Thread robotsServerThread;

	@Override
	public void start(final Stage primaryStage) throws Exception {
		RobotsServer robotsServer = new RobotsServer(RobotsServer.Level.TASK_6);
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
			log.info("Game started");
			for (int round = 1; !Thread.interrupted(); round++) {
//				log.info("Round {}", round);
				gameManager.makeTurn();

				Thread.sleep(150);
			}
		} catch (IOException | InterruptedException e) {
			// ignore
		}
		
		// TODO (danielw, 21.8.17): graceful shutdown
		System.exit(0);
	}
}
