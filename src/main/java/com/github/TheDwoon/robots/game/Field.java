package com.github.TheDwoon.robots.game;

import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.items.Item;

import java.util.Objects;

public class Field {
    private final int x;
    private final int y;
    // material is also a hint to the client what texture to use.
    private final Material material;

    private LivingEntity occupant;
    private Item item;

    public Field(final int x, final int y, Material material) {
        this.x = x;
        this.y = y;
        this.material = Objects.requireNonNull(material);

        this.occupant = null;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final Material getMaterial() {
        return material;
    }

    public Entity getOccupant() {
        return occupant;
    }

    public void setOccupant(LivingEntity occupant) {
        occupant.setX(x);
        occupant.setY(y);
        this.occupant = occupant;
    }

    public LivingEntity removeOccupant() {
        LivingEntity occupant = this.occupant;
        this.occupant = null;
        return occupant;
    }

    public boolean isOccupied() {
        return occupant != null;
    }

    public Entity getItem() {
        return item;
    }

    public void setItem(Item item) {
        item.setX(x);
        item.setY(y);
        this.item = item;
    }

    public Item removeItem() {
        Item item = this.item;
        this.item = null;
        return item;
    }

    public boolean hasItem() {
        return item != null;
    }

    @Override
    public String toString() {
        return String.format("[(%d,%d)/%s]", x, y, material.name());
    }

}
