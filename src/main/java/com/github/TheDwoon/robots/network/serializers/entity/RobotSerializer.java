package com.github.TheDwoon.robots.network.serializers.entity;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.entity.RobotImpl;

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
        kryo.writeClassAndObject(output, object.getInventory());
    }

    @Override
    @SuppressWarnings("unchecked")
    public T read(Kryo kryo, Input input, Class<T> type) {
        long uuid = input.readLong();
        int x = input.readInt();
        int y = input.readInt();
        int maxHealth = input.readInt();
        int health = input.readInt();
        Inventory inventory = (Inventory) kryo.readClassAndObject(input);

        if (type == RobotImpl.class) {
            return (T) new RobotImpl(uuid, x, y, maxHealth, health, inventory);
        }
        return create(type, uuid, x, y, maxHealth, health, inventory);
    }

    private T create(Class<T> type, long uuid, int x, int y, int maxHealth, int health, Inventory inventory) {
        try {
            Constructor<T> constructor = type.getConstructor(long.class, int.class, int.class, int.class, int.class, Inventory.class);
            return constructor.newInstance(uuid, x, y, maxHealth, health, inventory);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            return create(type, uuid, x, y, maxHealth, health);
        }
    }
}
