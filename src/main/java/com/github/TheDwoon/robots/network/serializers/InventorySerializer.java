package com.github.TheDwoon.robots.network.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.items.Item;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by sigmar on 28.05.17.
 */
public class InventorySerializer<T extends Inventory> extends Serializer<T> {

	@Override
	public void write(Kryo kryo, Output output, T object) {
		output.writeLong(object.getUUID());
		kryo.writeObject(output, object.getItems());
	}

	@Override
	@SuppressWarnings("unchecked")
	public T read(Kryo kryo, Input input, Class<T> type) {
		long uuid = input.readLong();
		Item[] items = kryo.readObject(input, Item[].class);

		if (type == Inventory.class) {
			return (T) new Inventory(uuid, items);
		}
		try {
			Constructor<T> constructor = type.getConstructor(long.class, Item[].class);
			return constructor.newInstance(uuid, items);
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			try {
				Constructor<T> constructor = type.getConstructor();
				return constructor.newInstance();
			} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e1) {
				throw new KryoException("Could not find Constructor for class: " + type.getName());
			}
		}
	}
}
