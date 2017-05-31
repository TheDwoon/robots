package com.github.TheDwoon.robots.game.items;

import com.github.TheDwoon.robots.game.InventoryHolder;
import com.github.TheDwoon.robots.game.entity.Entity;

/**
 * Created by sigma_000 on 30.05.2017.
 */
public interface Item extends Entity {
    void pickUp(InventoryHolder entity);

    void use();
    
    void drop();
}
