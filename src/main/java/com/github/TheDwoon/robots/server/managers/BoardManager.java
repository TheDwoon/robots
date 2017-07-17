package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Material;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.lang.Math.*;

public class BoardManager {
    //	private static final int DEFAULT_WIDTH = 100;
//	private static final int DEFAULT_HEIGHT = 100;
    private static final Material DEFAULT_MATERIAL = Material.GRASS;
    private static final Material DEFAULT_BORDER = Material.VOID;

    private final long uuid;
    private final int width;
    private final int height;
    private final Field[][] fields;

    private final List<Field> spawnFields;

    private final Deque<BoardObserver> observers;

    public BoardManager(long uuid, final Field[][] fields) {
        this.fields = fields;
        this.width = fields.length;
        this.height = fields[0].length;
        this.uuid = uuid;

        spawnFields = new ArrayList<>();
        for (Field[] row : fields) {
            for (Field field : row) {
                if (field.getMaterial() == Material.SPAWN) {
                    spawnFields.add(field);
                }
            }
        }

        observers = new ConcurrentLinkedDeque<>();
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

    public void addObserver(BoardObserver observer) {
        GameManager.oberverExecutor.submit(() -> {
            observer.setSize(uuid, width, height);
            for (Field[] row: fields) {
                observer.updateFields(uuid, row);
            }
        });
        observers.add(observer);
    }

    public void removeObserver(BoardObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Field... updatedFields) {
        GameManager.oberverExecutor.submit(() -> observers.forEach(o -> o.updateFields(uuid, updatedFields)));
    }

    public boolean spawnLivingEntity(LivingEntity entity) {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            Field field;
            try {
                field = spawnFields.stream().filter(f -> !f.isOccupied()).findAny().get();
            } catch (NoSuchElementException e) {
                return false;
            }

            if (spawnLivingEntity(entity, field)) {
                return true;
            }
        }
        return false;
    }

    public boolean spawnLivingEntity(LivingEntity entity, int x, int y) {
        return spawnLivingEntity(entity, fields[x][y]);
    }

    public boolean spawnLivingEntity(LivingEntity entity, Field field) {
        synchronized (field) {
            if (field.isOccupied() || !field.isVisitable()) {
                return false;
            }
            field.setOccupant(entity);
        }
        notifyObservers(field);
        return true;
    }

    public void removeLivingEntity(LivingEntity entity) {
        Field field = fields[entity.getX()][entity.getY()];
        synchronized (field) {
            field.removeOccupant();
        }
        notifyObservers(field);
    }

    public Entity removeLivingEntity(int x, int y) {
        Field field = fields[x][y];
        Entity entity;
        synchronized (field) {
            entity = field.getOccupant();
            field.removeOccupant();
        }
        notifyObservers(field);
        return entity;
    }

    public boolean moveLivingEntity(LivingEntity entity, int targetX, int targetY) {
        if (!checkCoordinates(targetX, targetY)) {
            return false;
        }

        Field sourceField = fields[entity.getX()][entity.getY()];
        Field targetField = fields[targetX][targetY];

        Field[] lockOrder = getLockOrder(sourceField, targetField);
        synchronized (lockOrder[0]) {
            synchronized (lockOrder[1]) {
                if (targetField.isOccupied() || !targetField.isVisitable()) {
                    return false;
                }

                sourceField.removeOccupant();
                targetField.setOccupant(entity);
            }
        }
        notifyObservers(sourceField, targetField);
        return true;
    }

    public boolean moveLivingEntityRelative(LivingEntity entity, int relativeX, int relativeY) {
        return moveLivingEntity(entity, entity.getX() + relativeX, entity.getY() + relativeY);
    }

    public boolean checkCoordinates(int x, int y) {
        return (x >= 0 && x < width) && (y >= 0 && y < height);
    }

    public List<Field> getVisibleFields(int posX, int posY) {
        final List<Field> visibleFields = new ArrayList<>(16);

        final int xStart = max(0, posX - 2), xEnd = min(posX + 2, width);
        final int yStart = max(0, posY - 2), yEnd = min(posY + 2, height);

        for (int x = xStart; x < xEnd; x++) {
            for (int y = yStart; y < yEnd; y++) {
                if (abs(posX - x) + abs(posY - y) <= 2) {
                    visibleFields.add(fields[x][y]);
                }
            }
        }

        return visibleFields;
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
