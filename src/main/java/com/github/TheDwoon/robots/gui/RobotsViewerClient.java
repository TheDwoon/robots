package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.Board;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RobotsViewerClient extends Application {

	public static void main(final String[] args) {
		launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		Board board = new Board(5, 5);

		GameDisplay gameDisplay = new GameDisplay(board);

		primaryStage.setScene(new Scene(gameDisplay));
		// primaryStage.setFullScreen(true);
		primaryStage.show();
	}

}
