package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.entity.Entity;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Region;

import java.io.IOException;

public class BoardFieldDisplay extends FieldDisplay {

	public BoardFieldDisplay(final Field field, final Entity entity, final DoubleBinding fieldSize)
		throws IOException {
		super(Textures.lookup(field.getMaterial()), Textures.lookup(entity));
		minWidthProperty().setValue(Region.USE_PREF_SIZE);
		prefWidthProperty().bind(fieldSize);
		maxWidthProperty().setValue(Region.USE_PREF_SIZE);
		minHeightProperty().setValue(Region.USE_PREF_SIZE);
		prefHeightProperty().bind(fieldSize);
		maxHeightProperty().setValue(Region.USE_PREF_SIZE);
	}

	public void update(final Field field) {
		setBackgroundImage(Textures.lookup(field.getMaterial()));
	}

	public void setEntity(final Entity entity) {
		setEntityImage(Textures.lookup(entity));
	}

}
