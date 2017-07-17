package com.github.TheDwoon.robots.server.entity;

import com.github.TheDwoon.robots.game.InventoryHolder;
import com.github.TheDwoon.robots.game.items.Item;
import com.github.TheDwoon.robots.server.RobotsServer;

public class ServerItem extends ServerEntity {

	private final Item item;
	
	public ServerItem(RobotsServer server, Item item) {
		super(server, item);

		this.item = item;
	}

	public void pickUp(InventoryHolder entity) {
		item.pickUp(entity);
	}

	public void use() {
		item.use();
	}

	public void drop() {
		item.drop();
	}

	public final Item getItem() {
		return item;
	}
}
