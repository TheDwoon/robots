package com.github.TheDwoon.robots.network.serializers.entity;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.entity.LivingEntityImpl;

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
    }

    @Override
    public T read(Kryo kryo, Input input, Class<T> type) {
        long uuid = input.readLong();
        int x = input.readInt();
        int y = input.readInt();
        int maxHealth = input.readInt();
        int health = input.readInt();

        return create(type, uuid, x, y, maxHealth, health);
    }

    protected T create(Class<T> type, long uuid, int x, int y, int maxHealth, int health) {
        try {
            Constructor<T> constructor = type.getConstructor(long.class, int.class, int.class, int.class, int.class);
            return constructor.newInstance(uuid, x, y, maxHealth, health);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            return create(type, uuid, x, y);
        }
    }
}
