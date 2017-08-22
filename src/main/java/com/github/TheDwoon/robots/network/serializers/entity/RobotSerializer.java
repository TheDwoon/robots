package com.github.TheDwoon.robots.network.serializers.entity;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.field.Facing;
import com.github.TheDwoon.robots.game.entity.Robot;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by sigmar on 28.05.17.
 */
public class RobotSerializer<T extends Robot> extends LivingEntitySerializer<T> {

	@Override
	public void write(Kryo kryo, Output output, T object) {
		output.writeLong(object.getUUID());
		output.writeInt(object.getX());
		output.writeInt(object.getY());
		output.writeInt(object.getMaxHealth());
		output.writeInt(object.getHealth());
		kryo.writeObject(output, object.getFacing());
		output.writeInt(object.getScore());
	}

	@Override
	public T read(Kryo kryo, Input input, Class<T> type) {
		long uuid = input.readLong();
		int x = input.readInt();
		int y = input.readInt();
		int maxHealth = input.readInt();
		int health = input.readInt();
		Facing facing = kryo.readObject(input, Facing.class);
		int score = input.readInt();

		return create(type, uuid, x, y, maxHealth, health, facing, score);
	}

	protected T create(Class<T> type, long uuid, int x, int y, int maxHealth, int health,
			Facing facing, int score) {
		try {
			Constructor<T> constructor =
					type.getConstructor(long.class, int.class, int.class, int.class, int.class,
							Facing.class, int.class);
			return constructor.newInstance(uuid, x, y, maxHealth, health, facing, score);
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			return create(type, uuid, x, y, maxHealth, health, facing);
		}
	}
}
