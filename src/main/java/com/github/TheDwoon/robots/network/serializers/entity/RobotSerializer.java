package com.github.TheDwoon.robots.network.serializers.entity;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.github.TheDwoon.robots.game.Facing;
import com.github.TheDwoon.robots.game.entity.Robot;

/**
 * Created by sigmar on 28.05.17.
 */
public class RobotSerializer<T extends Robot> extends LivingEntitySerializer<T> {

    @Override
    @SuppressWarnings("unchecked")
    public T read(Kryo kryo, Input input, Class<T> type) {
        long uuid = input.readLong();
        int x = input.readInt();
        int y = input.readInt();
        int maxHealth = input.readInt();
        int health = input.readInt();
        Facing facing = kryo.readObject(input, Facing.class);

        if (type == Robot.class) {
            return (T) new Robot(uuid, x, y, maxHealth, health, facing);
        }
        return create(type, uuid, x, y, maxHealth, health, facing);
    }
}
