package com.github.TheDwoon.robots.network.serializers.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.items.Gun;
import com.github.TheDwoon.robots.game.items.Weapon;
import com.github.TheDwoon.robots.network.serializers.entity.EntitySerializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by sigma_000 on 18.07.2017.
 */
public class GunSerializer<T extends Gun> extends WeaponSerializer<T> {

	@Override public void write(Kryo kryo, Output output, T object) {
		output.writeLong(object.getUUID());
		output.writeInt(object.getX());
		output.writeInt(object.getY());
		output.writeInt(object.getRange());
		output.writeInt(object.getDamage());
		output.writeDouble(object.getPiercingLoss());
		output.writeInt(object.getRoundsLeft());
	}

	@Override public T read(Kryo kryo, Input input, Class<T> type) {
		long uuid = input.readLong();
		int x = input.readInt();
		int y = input.readInt();
		int range = input.readInt();
		int damage = input.readInt();
		double piercingLoss = input.readDouble();
		int roundsLeft = input.readInt();

		return create(type, uuid, x, y, range, damage, piercingLoss, roundsLeft);
	}

	protected final T create(Class<T> type, long uuid, int x, int y, int range, int damage,
			double piercingLoss, int roundsLeft) {
		try {
			Constructor<T> constructor =
					type.getConstructor(long.class, int.class, int.class, int.class, int.class,
							double.class, int.class);
			return constructor.newInstance(uuid, x, y, range, damage, piercingLoss, roundsLeft);
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			return create(type, uuid, x, y, range, damage, piercingLoss);
		}
	}
}
