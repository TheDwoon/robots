package com.github.TheDwoon.robots.game;

import com.github.TheDwoon.robots.game.items.Item;

/**
 * Created by sigma_000 on 30.05.2017.
 */
public interface Inventory {
    long getUUID();

    Item[] getItems();

    int addItem(Item item);

    void removeItem(int slot);

    Item getItem(int slot);

    int size();
}
