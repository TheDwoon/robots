package com.github.TheDwoon.robots.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.entity.LivingEntity;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.interaction.AiObserver;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.game.items.Gun;
import com.github.TheDwoon.robots.game.items.Weapon;
import com.github.TheDwoon.robots.network.serializers.FieldSerializer;
import com.github.TheDwoon.robots.network.serializers.InventorySerializer;
import com.github.TheDwoon.robots.network.serializers.entity.EntitySerializer;
import com.github.TheDwoon.robots.network.serializers.entity.LivingEntitySerializer;
import com.github.TheDwoon.robots.network.serializers.entity.RobotSerializer;
import com.github.TheDwoon.robots.network.serializers.items.GunSerializer;
import com.github.TheDwoon.robots.network.serializers.items.WeaponSerializer;

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
		kryo.register(AiObserver.class);
		kryo.register(InventoryObserver.class);
		kryo.register(Field.class, new FieldSerializer());
		kryo.addDefaultSerializer(Robot.class, RobotSerializer.class);
		kryo.addDefaultSerializer(LivingEntity.class, LivingEntitySerializer.class);
		kryo.addDefaultSerializer(Entity.class, EntitySerializer.class);
		kryo.addDefaultSerializer(Inventory.class, InventorySerializer.class);
		kryo.addDefaultSerializer(Weapon.class, WeaponSerializer.class);
		kryo.addDefaultSerializer(Gun.class, GunSerializer.class);
	}
}
