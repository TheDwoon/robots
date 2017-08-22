package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.field.Field;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.ImageView;
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

		if (field.isOccupied()) {
			setEntity(field.getOccupant());
		} else if (field.hasItem()) {
			setEntity(field.getItem());
		} else {
			setEntity(null);
		}
	}

	public void setEntity(final Entity entity) {
		ImageView entityImage = Textures.lookup(entity);
		if (entity instanceof LivingEntity) {
			switch (((LivingEntity) entity).getFacing()) {
			case NORTH:
				entityImage.setRotate(180);
				break;
			case WEST:
				entityImage.setRotate(90);
				break;
			case SOUTH:
				entityImage.setRotate(0);
				break;
			case EAST:
				entityImage.setRotate(-90);
				break;
			}
		}
		setEntityImage(entityImage);
	}

}
