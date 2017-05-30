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

    protected FieldDisplay(final ImageView backgroundImage, final ImageView entityImage)
            throws IOException {
        Textures.bindSize(this, backgroundImage);
        Textures.bindSize(this, entityImage);
        getChildren().setAll(backgroundImage, entityImage);
    }

    protected void setBackgroundImage(final ImageView backgroundImage) {
        Textures.bindSize(this, backgroundImage);
        getChildren().set(0, backgroundImage);
    }

    protected void setEntityImage(final ImageView entityImage) {
        Textures.bindSize(this, entityImage);
        getChildren().set(1, entityImage);
    }

}
