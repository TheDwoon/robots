package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.items.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public final class RobotDisplay extends HBox {

	@FXML
	private ImageView robot;
	@FXML
	private Label robotName;
	@FXML
	private GridPane inventory;

	public RobotDisplay(final Robot robot) throws IOException {
		FXMLUtils.loadFxRoot(this);

		// TODO (sigmarw, 02.05.2017): obtain robot image
		robotName.setText(Long.toHexString(robot.getUUID()));
		inventory.setGridLinesVisible(true);
	}

	public void setInventory(Inventory inventory) {
		//  TODO (sigmarw, 27.05.17): implement
		//  TODO (sigmarw, 27.05.17): null check
	}

	public void updateItem(int slot, Item item) {
		//  TODO (sigmarw, 27.05.17): implement
	}

}
