package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.Material;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.interaction.AiObserver;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.game.items.Item;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.floor;
import static java.lang.Math.min;

public final class GameDisplay extends HBox
		implements BoardObserver, AiObserver, InventoryObserver {

	private static final Logger log = LogManager.getLogger();

	@FXML private Pane gameBoardContainer;
	@FXML private GridPane gameBoard;
	@FXML private VBox robotsContainer;

	private DoubleBinding fieldSize;

	private BoardFieldDisplay[][] boardFieldDisplays;
	private Map<Long, RobotDisplay> inventories;
	private Map<Long, RobotDisplay> robotDisplays;

	public GameDisplay() throws IOException {
		FXMLUtils.loadFxRoot(this);
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
				return floor(min(gameBoardContainer.widthProperty().getValue()
						/ boardFieldDisplays.length, gameBoardContainer.heightProperty().getValue()
						/ boardFieldDisplays[0].length));
			}

			@Override
			public ObservableList<?> getDependencies() {
				return FXCollections.observableArrayList(gameBoardContainer.widthProperty(),
						gameBoardContainer.heightProperty());
			}
		};

		boardFieldDisplays = new BoardFieldDisplay[0][0];
		inventories = new HashMap<>();
		robotDisplays = new HashMap<>();
	}

	@Override
	public void setSize(long uuid, int width, int height) {
		boardFieldDisplays = new BoardFieldDisplay[width][height];

		gameBoard.getChildren().clear();
		try {
			for (int x = 0; x < boardFieldDisplays.length; x++) {
				for (int y = 0; y < boardFieldDisplays[x].length; y++) {
					// fill with default values
					BoardFieldDisplay boardFieldDisplay =
							new BoardFieldDisplay(new Field(x, y, Material.VOID), null, fieldSize);
					boardFieldDisplays[x][y] = boardFieldDisplay;
				}
			}
			for (int x = 0; x < boardFieldDisplays.length; x++) {
				gameBoard.addColumn(x, boardFieldDisplays[x]);
			}
		} catch (IOException e) {
			log.catching(e);
		}
	}

	@Override
	public void updateFields(long uuid, Field[] fields) {
		for (Field field : fields) {
			boardFieldDisplays[field.getX()][field.getY()].update(field);
		}
	}

	@Override
	public void createInventory(long inventoryHolderUuid, Inventory inventory) {
		RobotDisplay robotDisplay = robotDisplays.entrySet().parallelStream()
				.filter(entry -> entry.getKey() == inventoryHolderUuid).map(Map.Entry::getValue)
				.findAny().orElse(null);
		inventories.put(inventory.getUUID(), robotDisplay);
		if (robotDisplay != null) {
			try {
				robotDisplay.setInventory(inventory);
			} catch (IOException e) {
				log.catching(e);
			}
		}
	}

	@Override
	public void deleteInventory(long uuid) {
		RobotDisplay robotDisplay = inventories.remove(uuid);
		if (robotDisplay != null) {
			try {
				robotDisplay.setInventory(null);
			} catch (IOException e) {
				log.catching(e);
			}
		}
	}

	@Override
	public void updateInventory(long uuid, int slot, Item item) {
		RobotDisplay robotDisplay = inventories.get(uuid);
		if (robotDisplay != null) {
			robotDisplay.updateItem(slot, item);
		}
	}

	@Override
	public void spawnAi(Robot robot, Inventory inventory) {
		RobotDisplay robotDisplay;
		try {
			robotDisplay = new RobotDisplay(robot, inventory);
		} catch (IOException e) {
			log.catching(e);
			return;
		}
		robotDisplays.put(robot.getUUID(), robotDisplay);
		inventories.put(inventory.getUUID(), robotDisplay);
		robotsContainer.getChildren().add(robotDisplay);
	}

	@Override
	public void despawnAi(long robotUuid, long inventoryUuid) {
		RobotDisplay robotDisplay = robotDisplays.remove(robotUuid);
		inventories.remove(inventoryUuid);
		robotsContainer.getChildren().remove(robotDisplay);
		Textures.removeRobot(robotUuid);
	}
}
