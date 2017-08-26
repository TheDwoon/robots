package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Inventory;
import com.github.TheDwoon.robots.game.items.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public final class RobotDisplay extends VBox {

	@FXML
	private StackPane robotImageContainer;
	@FXML
	private Label robotName;
	@FXML
	private Label score;
	@FXML
	private Label health;
	@FXML
	private Label maxHealth;
	@FXML
	private TilePane inventoryContainer;

	public RobotDisplay(final Robot robot, Inventory inventory) throws IOException {
		FXMLUtils.loadFxRoot(this);

		setRobotImage(Textures.lookup(robot));
		robotName.setText(robot.getName());
		score.setText(Integer.toString(robot.getScore()));
		health.setText(Integer.toString(robot.getHealth()));
		maxHealth.setText(Integer.toString(robot.getMaxHealth()));
		setInventory(inventory);
	}

	public void setRobotImage(ImageView robotImage) {
		Textures.bindSize(robotImageContainer, robotImage);
		this.robotImageContainer.getChildren().set(0, robotImage);
	}

	public void setInventory(Inventory inventory) throws IOException {
		inventoryContainer.getChildren().clear();
		if (inventory == null) {
			return;
		}
		for (Item item : inventory.getItems()) {
			inventoryContainer.getChildren().add(new InventoryFieldDisplay(item));
		}
	}

	public void updateItem(int slot, Item item) {
		((InventoryFieldDisplay) inventoryContainer.getChildren().get(slot)).setItem(item);
	}

	public void updateScore(int score) {
		this.score.setText(Integer.toString(score));
	}

	public void updateHealth(int health) {
		this.health.setText(Integer.toString(health));
	}

}
