package com.github.TheDwoon.robots.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.server.RobotsServer;

public class Board {
    //	private static final int DEFAULT_WIDTH = 100;
//	private static final int DEFAULT_HEIGHT = 100;
    private static final Material DEFAULT_MATERIAL = Material.GRASS;
    private static final Material DEFAULT_BORDER = Material.VOID;

    private final long uuid;
    private final int width;
    private final int height;
    private final Field[][] fields;

    public Board(long uuid, final int width, final int height) {
        this.width = width;
        this.height = height;
        this.fields = new Field[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
                    fields[x][y] = new Field(x, y, DEFAULT_BORDER);
                else
                    fields[x][y] = new Field(x, y, DEFAULT_MATERIAL);
            }
        }
        this.uuid = uuid;
    }

    public Board(long uuid, final Field[][] fields) {
        this.fields = fields;
        this.width = fields.length;
        this.height = fields[0].length;
        this.uuid = uuid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Field getField(int x, int y) {
        return fields[x][y];
    }

    public void spawnLivingEntity(LivingEntity entity, int x, int y) {
        Field field = fields[x][y];
        synchronized (field) {
            entity.setPosition(x, y);
            field.setOccupant(entity);
        }
    }

    public void removeLivingEntity(LivingEntity entity) {
        Field field = fields[entity.getX()][entity.getY()];
        synchronized (field) {
            field.removeOccupant();
        }
    }

    public Entity removeLivingEntity(int x, int y) {
        Field field = fields[x][y];
        Entity entity;
        synchronized (field) {
            entity = field.getOccupant();
            field.removeOccupant();
        }
        return entity;
    }

    public boolean moveLivingEntity(LivingEntity entity, int targetX, int targetY) {
        if (!checkCoordinates(targetX, targetY)) {
            return false;
        }

        Field sourceField = fields[entity.getX()][entity.getY()];
        Field targetField = fields[targetX][targetY];

        if (targetField.isOccupied()) {
            return false;
        }

        Field[] lockOrder = getLockOrder(sourceField, targetField);
        synchronized (lockOrder[0]) {
            synchronized (lockOrder[1]) {
                sourceField.removeOccupant();
                targetField.setOccupant(entity);
            }
        }
        return true;
    }

    public boolean moveLivingEntityRelative(LivingEntity entity, int relativeX, int relativeY) {
        return moveLivingEntity(entity, entity.getX() + relativeX, entity.getY() + relativeY);
    }

    public boolean checkCoordinates(int x, int y) {
        return (x >= 0 && x < width) && (y >= 0 && y < width);
    }

    private Field[] getLockOrder(final Field... fields) {
        Field[] fieldsSorted = Arrays.copyOf(fields, fields.length);
        Arrays.sort(fieldsSorted, Comparator.comparingInt(f -> f.getX() * width + f.getY()));
        return fieldsSorted;
    }

    public final long getUUID() {
        return uuid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Field[] row : fields) {
            for (int y = 0; y < row.length; y++) {
                sb.append(row[y]);
                if (y < row.length - 1) {
                    sb.append(' ');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
