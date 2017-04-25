package com.github.TheDwoon.robots.network.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.entity.Entity;
import com.github.TheDwoon.robots.game.items.Item;

public class FieldSerializer extends Serializer<Field> {

	@Override
	public void write(final Kryo kryo, final Output output, final Field object) {
		output.writeInt(object.getX());
		output.writeInt(object.getY());
		kryo.writeClassAndObject(output, object.getItem());
		output.writeBoolean(object.isVisitable());
		kryo.writeClassAndObject(output, object.getVisitor());
	}

	@Override
	public Field read(final Kryo kryo, final Input input, final Class<Field> type) {
		int x = input.readInt();
		int y = input.readInt();
		Item item = (Item) kryo.readClassAndObject(input);
		boolean visitable = input.readBoolean();
		Entity visitor = (Entity) kryo.readClassAndObject(input);
		return new Field(x, y, item, visitable, visitor);
	}

}
