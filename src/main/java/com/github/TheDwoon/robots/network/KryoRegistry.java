package com.github.TheDwoon.robots.network;

import com.esotericsoftware.kryo.Kryo;
import com.github.TheDwoon.robots.client.UpdateHandler;

public class KryoRegistry {

	/**
	 * Utility class
	 */
	private KryoRegistry() {}

	public static final void register(final Kryo kryo) {
		kryo.setRegistrationRequired(true);
		kryo.register(UpdateHandler.class);
		kryo.register(int[].class);
	}
}
