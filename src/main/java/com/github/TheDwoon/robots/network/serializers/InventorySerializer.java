package com.github.TheDwoon.robots.network.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.Inventory;
import com.github.TheDwoon.robots.game.InventoryImpl;
import com.github.TheDwoon.robots.game.items.Item;

/**
 * Created by sigmar on 28.05.17.
 */
public class InventorySerializer extends Serializer<Inventory> {

    @Override
    public void write(Kryo kryo, Output output, Inventory object) {
        output.writeLong(object.getUUID());
        kryo.writeObject(output, object.getItems());
    }

    @Override
    public Inventory read(Kryo kryo, Input input, Class<Inventory> type) {
        long uuid = input.readLong();
        Item[] items = kryo.readObject(input, Item[].class);
        return new InventoryImpl(uuid, items);
    }
}
