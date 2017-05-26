package com.github.TheDwoon.robots.gui;

import java.io.IOException;

import com.github.TheDwoon.robots.game.Material;
import com.github.TheDwoon.robots.game.entity.Entity;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public abstract class FieldDisplay extends StackPane {

	@FXML
	private ImageView background;
	@FXML
	private ImageView entity;

	protected FieldDisplay(final Image backgroundImage, final Image entityImage)
		throws IOException {
		FXMLUtils.loadFxRoot(this, FieldDisplay.class);
		widthProperty().addListener((observable, oldValue, newValue) -> {
			background.setFitWidth(newValue.doubleValue());
			entity.setFitWidth(newValue.doubleValue());
		});
		heightProperty().addListener((observable, oldValue, newValue) -> {
			background.setFitHeight(newValue.doubleValue());
			entity.setFitHeight(newValue.doubleValue());
		});
		background.setImage(backgroundImage);
		entity.setImage(entityImage);
	}

	protected void setEntityImage(final Image entityImage) {
		entity.setImage(entityImage);
	}

	protected static Image lookupTexture(final Material material) {
		return new Image(FieldDisplay.class.getResourceAsStream(
			"/textures/material_" + material.toString().toLowerCase() + ".png"));
	}

	protected static Image lookupTexture(final Entity material) {
		// TODO (sigmarw, 02.05.2017): implement
		// TODO (sigmarw, 02.05.2017): Null check!
		return null;
	}

	protected static Image lookupInventoryBackgroundTexture() {
		return new Image(FieldDisplay.class.getResourceAsStream("textures/inventory.png"));
	}
}