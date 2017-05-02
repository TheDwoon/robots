package com.github.TheDwoon.robots.gui;

import java.io.IOException;

import com.github.TheDwoon.robots.game.items.Item;

public class InventoryFieldDisplay extends FieldDisplay {

	public InventoryFieldDisplay(final Item item) throws IOException {
		super(lookupInventoryBackgroundTexture(), lookupTexture(item));
	}

	public void setItem(final Item item) {
		setEntityImage(lookupTexture(item));
	}
}
