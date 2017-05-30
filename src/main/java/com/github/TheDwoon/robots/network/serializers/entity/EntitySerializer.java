package com.github.TheDwoon.robots.network.serializers.entity;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.EntityImpl;

/**
 * Created by sigmar on 28.05.17.
 */
public class EntitySerializer extends Serializer<Entity> {

    @Override
    public void write(Kryo kryo, Output output, Entity object) {
        output.writeLong(object.getUUID());
        output.writeInt(object.getX());
        output.writeInt(object.getY());
    }

    @Override
    public Entity read(Kryo kryo, Input input, Class<Entity> type) {
        long uuid = input.readLong();
        int x = input.readInt();
        int y = input.readInt();
        return new EntityImpl(uuid, x, y) {};
    }
}
