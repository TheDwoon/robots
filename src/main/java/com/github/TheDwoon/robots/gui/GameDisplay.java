package com.github.TheDwoon.robots.gui;

import static java.lang.Math.min;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Material;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Item;

import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public final class GameDisplay extends HBox {

	@FXML
	private Pane gameBoardContainer;
	@FXML
	private GridPane gameBoard;
	@FXML
	private VBox robots;

	private DoubleBinding fieldSize;

	private BoardFieldDisplay[][] boardFieldDisplays;
	private Map<Robot, RobotDisplay> robotDisplays;

	public GameDisplay(final Board board) throws IOException {
		FXMLUtils.loadFxRoot(this);

		// TODO (sigmarw, 02.05.2017): initialize with board
		// TODO (sigmarw, 02.05.2017): remove dummy
		boardFieldDisplays = new BoardFieldDisplay[15][10];

		fieldSize = new DoubleBinding() {
			{
				super.bind(gameBoardContainer.widthProperty(), gameBoardContainer.heightProperty());
			}

			@Override
			public void dispose() {
				super.unbind(gameBoardContainer.widthProperty(),
					gameBoardContainer.heightProperty());
			}

			@Override
			protected double computeValue() {
				return min(
					gameBoardContainer.widthProperty().getValue() / boardFieldDisplays.length,
					gameBoardContainer.heightProperty().getValue() / boardFieldDisplays[0].length);
			}

			@Override
			public ObservableList<?> getDependencies() {
				return FXCollections.observableArrayList(gameBoardContainer.widthProperty(),
					gameBoardContainer.heightProperty());
			}
		};
		for (int x = 0; x < boardFieldDisplays.length; x++) {
			gameBoard.addColumn(x);
		}
		for (int y = 0; y < boardFieldDisplays[0].length; y++) {
			gameBoard.addRow(y);
		}
		for (int x = 0; x < boardFieldDisplays.length; x++) {
			for (int y = 0; y < boardFieldDisplays[x].length; y++) {
				// TODO (sigmarw, 02.05.2017): remove dummy
				BoardFieldDisplay boardFieldDisplay =
					new BoardFieldDisplay(new Field(x, y, Material.GRASS), null, fieldSize);
				boardFieldDisplays[x][y] = boardFieldDisplay;
				gameBoard.add(boardFieldDisplay, x, y);
			}
		}
		// TODO (sigmarw, 02.05.2017): remove debug
		gameBoard.setGridLinesVisible(true);

		robotDisplays = new HashMap<>();
		addRobot(new Robot(3, 3, null, null));

	}

	public void updateEntities(final Entity[] update) {
		// TODO (sigmarw, 02.05.2017): implement
	}

	public void addRobot(final Robot robot) throws IOException {
		RobotDisplay robotDisplay = new RobotDisplay(robot);
		robotDisplays.put(robot, robotDisplay);
		robots.getChildren().add(robotDisplay);
		addToBoard(robot);
	}

	public void removeRobot(final Robot robot) {
		RobotDisplay robotDisplay = robotDisplays.remove(robot);
		if (robotDisplay != null) {
			robots.getChildren().remove(robotDisplay);
		}
		removeFromBoard(robot);
	}

	public void addItem(final Item item) {
		addToBoard(item);
	}

	public void removeItem(final Item item) {
		removeFromBoard(item);
	}

	private void addToBoard(final Entity entity) {
		boardFieldDisplays[entity.getX()][entity.getY()].setEntity(entity);
	}

	private void removeFromBoard(final Entity entity) {
		boardFieldDisplays[entity.getX()][entity.getY()].setEntity(null);
	}

	public void removeRobot(final String robotName) {
		// TODO (sigmarw, 02.05.2017): implement
	}

}
