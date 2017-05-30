package com.github.TheDwoon.robots.network.serializers.entity;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.entity.RobotImpl;
import com.github.TheDwoon.robots.game.items.weapons.Weapon;

/**
 * Created by sigmar on 28.05.17.
 */
public class RobotSerializer extends Serializer<Robot> {

    @Override
    public void write(Kryo kryo, Output output, Robot object) {
        output.writeLong(object.getUUID());
        output.writeInt(object.getX());
        output.writeInt(object.getY());
        output.writeInt(object.getMaxHealth());
        output.writeInt(object.getHealth());
        kryo.writeClassAndObject(output, object.getInventory());
        kryo.writeClassAndObject(output, object.getWeapon());
    }

    @Override
    public Robot read(Kryo kryo, Input input, Class<Robot> type) {
        long uuid = input.readLong();
        int x = input.readInt();
        int y = input.readInt();
        int maxHealth = input.readInt();
        int health = input.readInt();
        Inventory inventory = (Inventory) kryo.readClassAndObject(input);
        Weapon weapon = (Weapon) kryo.readClassAndObject(input);
        return new RobotImpl(uuid, x, y, maxHealth, health, inventory, weapon);
    }
}
