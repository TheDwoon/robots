package com.github.TheDwoon.robots.gui;

import com.github.TheDwoon.robots.game.items.Item;

import java.io.IOException;

public class InventoryFieldDisplay extends FieldDisplay {

	public InventoryFieldDisplay(final Item item) throws IOException {
		super(Textures.lookupInventoryBackground(), Textures.lookup(item));
		setWidth(25);
		setHeight(25);
	}

	public void setItem(final Item item) {
		setEntityImage(Textures.lookup(item));
	}
}
