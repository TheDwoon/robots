package com.github.TheDwoon.robots.server.managers;

import com.github.TheDwoon.robots.game.board.Facing;
import com.github.TheDwoon.robots.game.board.Field;
import com.github.TheDwoon.robots.game.board.Material;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.items.Item;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class BoardManager {

	private Random random;

	private final long uuid;
	private final int width;
	private final int height;
	private final Field[][] fields;

	private final Deque<BoardObserver> observers;

	private final List<Field> spawnFields;
	private final List<Field> itemFields;

	private final SpawnQueueManager spawnQueueManager;

	public BoardManager(long uuid, final Field[][] fields) {
		this.random = new Random();

		this.fields = fields;
		this.width = fields.length;
		this.height = fields[0].length;
		this.uuid = uuid;

		observers = new ConcurrentLinkedDeque<>();

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
		// no spawn fields -> all visitable fields become spawn fields
		if (spawnFields.isEmpty()) {
			spawnFields.addAll(itemFields);
		}

		spawnQueueManager = new SpawnQueueManager();
		observers.add(spawnQueueManager);
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

	public Field getFieldChecked(int x, int y) {
		if (!checkCoordinates(x, y)) {
			return null;
		}
		return fields[x][y];
	}

	public void addObserver(BoardObserver observer) {
		GameManager.observerExecutor.submit(() -> {
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
		observers.forEach(o -> GameManager.observerExecutor
				.submit(() -> o.updateFields(uuid, updatedFields)));
	}

	/**
	 * Spawns a {@link LivingEntity} on the board.
	 *
	 * @param entity {@link LivingEntity} to spawn
	 * @return false, if the entity could not be spawned immediately and was therefor added to the spawn queue
	 */
	public boolean spawnLivingEntity(LivingEntity entity) {
		Field field;
		Field[] possibleFields =
				spawnFields.parallelStream().filter(f -> !f.isOccupied()).toArray(Field[]::new);
		if (possibleFields.length == 0) {
			spawnQueueManager.addToQueue(entity);
			return false;
		}
		field = possibleFields[random.nextInt(possibleFields.length)];

		if (!spawnLivingEntity(entity, field)) {
			spawnQueueManager.addToQueue(entity);
			return false;
		}
		return true;
	}

	/**
	 * Spawns a {@link LivingEntity} on the specified board.
	 *
	 * @param entity {@link LivingEntity} to spawn
	 * @param x      x coordinate to spawn it on
	 * @param y      y coordinate to spawn it on
	 * @return false, if the entity could not be spawned on the specified board.
	 * Note that the entity is <u>not added to the spawn queue</u>.
	 */
	public boolean spawnLivingEntity(LivingEntity entity, int x, int y) {
		return spawnLivingEntity(entity, fields[x][y]);
	}

	/**
	 * Spawns a {@link LivingEntity} on the specified board.
	 *
	 * @param entity {@link LivingEntity} to spawn
	 * @param field  {@link Field} to spawn it on
	 * @return false, if the entity could not be spawned on the specified board.
	 * Note that the entity is <u>not added to the spawn queue</u>.
	 */
	private boolean spawnLivingEntity(LivingEntity entity, Field field) {
		synchronized (field) {
			if (field.isOccupied() || !field.isVisitable()) {
				return false;
			}
			field.setOccupant(entity);
			entity.fullHeal();
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

		Field[] lockingOrder = getLockingOrder(sourceField, targetField);
		synchronized (lockingOrder[0]) {
			synchronized (lockingOrder[1]) {
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

	public void damageLivingEntity(int x, int y, int damage, Entity origin) {
		Field field = fields[x][y];
		synchronized (field) {
			if (!field.isOccupied()) {
				return;
			}

			LivingEntity occupant = field.getOccupant();
			occupant.damage(damage, origin);
			if (occupant.isDead()) {
				field.removeOccupant();
			}
		}
		notifyObservers(field);
	}

	public boolean spawnItem(Item item) {
		for (int i = 0; i < 3; i++) {
			Field field;
			try {
				Field[] possibleFields =
						itemFields.parallelStream().filter(f -> !f.hasItem()).toArray(Field[]::new);
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

		for (int x = max(0, posX - 3); x < min(posX + 4, width); x++) {
			for (int y = max(0, posY - 3); y < min(posY + 4, height); y++) {
				visibleFields.add(fields[x][y]);
			}
		}

		return visibleFields;
	}

	private Field[] getLockingOrder(final Field... fields) {
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

	private class SpawnQueueManager implements BoardObserver {
		private final Queue<LivingEntity> spawnQueue;

		public SpawnQueueManager() {
			spawnQueue = new LinkedList<>();
		}

		public synchronized void addToQueue(LivingEntity entity) {
			spawnQueue.add(entity);
		}

		@Override
		public void setSize(long uuid, int width, int height) {
			// do nothing
		}

		@Override
		public synchronized void updateFields(long uuid, Field[] fields) {
			if (spawnQueue.isEmpty()) {
				return;
			}
			// TODO (sigmarw, 22.08.2017): performance optimization?
			for (Field field : fields) {
				if (!spawnFields.contains(field)) {
					continue;
				}
				synchronized (field) {
					LivingEntity entity = spawnQueue.element();
					boolean spawned = spawnLivingEntity(entity, field);
					if (!spawned) {
						continue;
					}

					LivingEntity removed = spawnQueue.remove();
					assert entity == removed;
					if (spawnQueue.isEmpty()) {
						break;
					}
				}
			}
		}
	}
}
