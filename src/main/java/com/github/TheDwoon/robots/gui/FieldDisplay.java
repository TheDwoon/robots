package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.Material;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.Robot;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

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

    protected void setBackgroundImage(final Image backgroundImage) {
        background.setImage(backgroundImage);
    }

    protected void setEntityImage(final Image entityImage) {
        entity.setImage(entityImage);
    }

}
