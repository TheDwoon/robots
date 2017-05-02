package com.github.TheDwoon.robots.gui;

import java.io.IOException;

import com.github.TheDwoon.robots.game.entity.Robot;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

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

}
