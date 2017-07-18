package com.github.TheDwoon.robots.network.serializers.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
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
public class WeaponSerializer<T extends Weapon> extends EntitySerializer<T> {

    @Override
    public void write(Kryo kryo, Output output, T object) {
        output.writeLong(object.getUUID());
        output.writeInt(object.getX());
        output.writeInt(object.getY());
        output.writeInt(object.getRange());
        output.writeInt(object.getDamage());
        output.writeDouble(object.getPiercingLoss());
    }

    @Override
    public T read(Kryo kryo, Input input, Class<T> type) {
        long uuid = input.readLong();
        int x = input.readInt();
        int y = input.readInt();
        int range = input.readInt();
        int damage = input.readInt();
        double piercingLoss = input.readDouble();

        return create(type, uuid, x, y, range, damage, piercingLoss);
    }

    protected final T create(Class<T> type, long uuid, int x, int y, int range, int damage, double piercingLoss) {
        try {
            Constructor<T> constructor = type.getConstructor(long.class, int.class, int.class, int.class, int.class, double.class);
            return constructor.newInstance(uuid, x, y, range, damage, piercingLoss);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            return create(type, uuid, x, y);
        }
    }
}
