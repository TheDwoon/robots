package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.Facing;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Material;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.items.Item;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.lang.Math.*;

public class BoardManager {
    private static final Random random = new Random();

    private final long uuid;
    private final int width;
    private final int height;
    private final Field[][] fields;

    private final List<Field> spawnFields;
    private final List<Field> itemFields;

    private final Deque<BoardObserver> observers;

    public BoardManager(long uuid, final Field[][] fields) {
        this.fields = fields;
        this.width = fields.length;
        this.height = fields[0].length;
        this.uuid = uuid;

        spawnFields = new ArrayList<>();
        itemFields = new ArrayList<>();
        for (Field[] row : fields) {
            for (Field field : row) {
                if (field.getMaterial() == Material.SPAWN) {
                    spawnFields.add(field);
                } else if (field.isVisitable()) {
                    itemFields.add(field);
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
            for (Field[] row : fields) {
                observer.updateFields(uuid, row);
            }
        });
        observers.add(observer);
    }

    public void removeObserver(BoardObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Field... updatedFields) {
        observers.forEach(o -> GameManager.oberverExecutor.submit(() -> o.updateFields(uuid, updatedFields)));
    }

    public boolean spawnLivingEntity(LivingEntity entity) {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            Field field;
            try {
                Field[] possibleFields = spawnFields.parallelStream().filter(f -> !f.isOccupied()).toArray(Field[]::new);
                field = possibleFields[random.nextInt(possibleFields.length)];
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

    private boolean spawnLivingEntity(LivingEntity entity, Field field) {
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

    public LivingEntity removeLivingEntity(int x, int y) {
        Field field = fields[x][y];
        LivingEntity entity;
        synchronized (field) {
            entity = field.removeOccupant();
        }
        if (entity != null) {
            notifyObservers(field);
        }
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

    public void turnLivingEntity(LivingEntity entity, Facing facing) {
        entity.setFacing(facing);
        notifyObservers(fields[entity.getX()][entity.getY()]);
    }

    public boolean spawnItem(Item item) {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            Field field;
            try {
                Field[] possibleFields = itemFields.parallelStream().filter(f -> !f.hasItem()).toArray(Field[]::new);
                field = possibleFields[random.nextInt(possibleFields.length)];
            } catch (NoSuchElementException e) {
                return false;
            }

            if (spawnItem(item, field)) {
                return true;
            }
        }
        return false;
    }

    public boolean spawnItem(Item item, int x, int y) {
        return spawnItem(item, fields[x][y]);
    }

    private boolean spawnItem(Item item, Field field) {
        synchronized (field) {
            if (field.hasItem()) {
                return false;
            }
            field.setItem(item);
        }
        notifyObservers(field);
        return true;
    }

    public void removeItem(Item item) {
        Field field = fields[item.getX()][item.getY()];
        synchronized (field) {
            field.removeItem();
        }
        notifyObservers(field);
    }

    public Item removeItem(int x, int y) {
        Field field = fields[x][y];
        Item item;
        synchronized (field) {
            item = field.removeItem();
        }
        if (item != null) {
            notifyObservers(field);
        }
        return item;
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
