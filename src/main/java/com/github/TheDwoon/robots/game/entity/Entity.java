package com.github.TheDwoon.robots.game.entity;

import com.github.TheDwoon.robots.game.Board;

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

    Board getBoard();

    void setBoard(Board board);

    void update();

    String getType();
}
