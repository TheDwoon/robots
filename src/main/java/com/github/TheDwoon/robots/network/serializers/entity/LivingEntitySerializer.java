package com.github.TheDwoon.robots.network.serializers.entity;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.entity.LivingEntityImpl;

/**
 * Created by sigmar on 28.05.17.
 */
public class LivingEntitySerializer extends Serializer<LivingEntity> {

    @Override
    public void write(Kryo kryo, Output output, LivingEntity object) {
        output.writeLong(object.getUUID());
        output.writeInt(object.getX());
        output.writeInt(object.getY());
        output.writeInt(object.getMaxHealth());
        output.writeInt(object.getHealth());
    }

    @Override
    public LivingEntity read(Kryo kryo, Input input, Class<LivingEntity> type) {
        long uuid = input.readLong();
        int x = input.readInt();
        int y = input.readInt();
        int maxHealth = input.readInt();
        int health = input.readInt();
        return new LivingEntityImpl(uuid, x, y, maxHealth, health) { };
    }
}
