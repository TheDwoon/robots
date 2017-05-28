package com.github.TheDwoon.robots.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.EntityObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.network.serializers.FieldSerializer;

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
    }
}
