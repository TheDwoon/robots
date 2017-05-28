package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

import java.io.IOException;

public final class RobotDisplay extends HBox {

	@FXML
	private ImageView robotImage;
	@FXML
	private Label robotName;
	@FXML
	private TilePane inventoryContainer;

	public RobotDisplay(final Robot robot) throws IOException {
		FXMLUtils.loadFxRoot(this);

		robotImage.setImage(Textures.lookup(robot));
		robotName.setText(Long.toHexString(robot.getUUID()));
		setInventory(robot.getInventory());
	}

	public void setInventory(Inventory inventory) throws IOException {
		inventoryContainer.getChildren().clear();
		if (inventory == null) {
			return;
		}
		for (Item item: inventory.getItems()) {
			inventoryContainer.getChildren().add(new InventoryFieldDisplay(item));
		}
	}

	public void updateItem(int slot, Item item) {
		((InventoryFieldDisplay) inventoryContainer.getChildren().get(slot)).setItem(item);
	}

}
