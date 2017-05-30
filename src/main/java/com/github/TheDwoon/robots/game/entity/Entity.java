package com.github.TheDwoon.robots.game.entity;

/**
 * Created by sigma_000 on 30.05.2017.
 */
public interface Entity {
    long getUUID();

    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    void setPosition(int x, int y);

    void update();

    default String getType() {
        return getClass().getSimpleName().replaceAll("Impl$", "");
    }
}
