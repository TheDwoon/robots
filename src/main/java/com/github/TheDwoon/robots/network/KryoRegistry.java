package com.github.TheDwoon.robots.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.EntityObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.network.serializers.InventorySerializer;
import com.github.TheDwoon.robots.network.serializers.entity.EntitySerializer;
import com.github.TheDwoon.robots.network.serializers.FieldSerializer;
import com.github.TheDwoon.robots.network.serializers.entity.LivingEntitySerializer;
import com.github.TheDwoon.robots.network.serializers.entity.RobotSerializer;

public class KryoRegistry {

    /**
     * Utility class
     */
    private KryoRegistry() {
    }

    public static final void register(final Kryo kryo) {
        ObjectSpace.registerClasses(kryo);

        kryo.setRegistrationRequired(false);

        kryo.addDefaultSerializer(Throwable.class, new ThrowableSerializer());

        kryo.register(BoardObserver.class);
        kryo.register(InventoryObserver.class);
        kryo.register(EntityObserver.class);
        kryo.register(Field.class, new FieldSerializer());
        kryo.addDefaultSerializer(Robot.class, new RobotSerializer());
        kryo.addDefaultSerializer(LivingEntity.class, new LivingEntitySerializer());
        kryo.addDefaultSerializer(Entity.class, new EntitySerializer());
        kryo.addDefaultSerializer(Inventory.class, new InventorySerializer());
    }
}
