package com.github.TheDwoon.robots.network.serializers.entity;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.field.Facing;
import com.github.TheDwoon.robots.game.entity.LivingEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by sigmar on 28.05.17.
 */
public class LivingEntitySerializer<T extends LivingEntity> extends EntitySerializer<T> {

	@Override
	public void write(Kryo kryo, Output output, T object) {
		output.writeLong(object.getUUID());
		output.writeInt(object.getX());
		output.writeInt(object.getY());
		output.writeInt(object.getMaxHealth());
		output.writeInt(object.getHealth());
		kryo.writeObject(output, object.getFacing());
	}

	@Override
	public T read(Kryo kryo, Input input, Class<T> type) {
		long uuid = input.readLong();
		int x = input.readInt();
		int y = input.readInt();
		int maxHealth = input.readInt();
		int health = input.readInt();
		Facing facing = kryo.readObject(input, Facing.class);

		return create(type, uuid, x, y, maxHealth, health, facing);
	}

	protected T create(Class<T> type, long uuid, int x, int y, int maxHealth, int health,
			Facing facing) {
		try {
			Constructor<T> constructor =
					type.getConstructor(long.class, int.class, int.class, int.class, int.class,
							Facing.class);
			return constructor.newInstance(uuid, x, y, maxHealth, health, facing);
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			return create(type, uuid, x, y);
		}
	}
}
