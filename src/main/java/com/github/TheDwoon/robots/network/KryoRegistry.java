package com.github.TheDwoon.robots.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.client.UpdateHandler;
import com.github.TheDwoon.robots.game.Field;

public class KryoRegistry {

	/**
	 * Utility class
	 */
	private KryoRegistry() {}

	public static final void register(final Kryo kryo) {
		ObjectSpace.registerClasses(kryo);

		kryo.setRegistrationRequired(false);

		kryo.addDefaultSerializer(Throwable.class, new ThrowableSerializer());

		kryo.register(UpdateHandler.class);
		kryo.register(Field.class, new FieldSerializer<Field>(kryo, Field.class));
	}
}
