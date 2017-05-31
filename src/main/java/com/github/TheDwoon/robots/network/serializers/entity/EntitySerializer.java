package com.github.TheDwoon.robots.network.serializers.entity;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by sigmar on 28.05.17.
 */
public class EntitySerializer<T extends Entity> extends Serializer<T> {

    public static final Logger log = LogManager.getLogger();

    @Override
    public void write(Kryo kryo, Output output, T object) {
        output.writeLong(object.getUUID());
        output.writeInt(object.getX());
        output.writeInt(object.getY());
    }

    @Override
    public T read(Kryo kryo, Input input, Class<T> type) {
        long uuid = input.readLong();
        int x = input.readInt();
        int y = input.readInt();

        return create(type, uuid, x, y);
    }

    protected final T create(Class<T> type, long uuid, int x, int y) {
        try {
            Constructor<T> constructor = type.getConstructor(long.class, int.class, int.class);
            return constructor.newInstance(uuid, x, y);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            return create(type);
        }
    }

    protected final T create(Class<T> type) {
        try {
            Constructor<T> constructor = type.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e1) {
            throw new KryoException("Could not find Constructor for class: " + type.getName());
        }
    }
}
