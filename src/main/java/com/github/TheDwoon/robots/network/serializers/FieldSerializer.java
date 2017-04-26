package com.github.TheDwoon.robots.network.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Material;

public class FieldSerializer extends Serializer<Field> {

	@Override
	public void write(final Kryo kryo, final Output output, final Field object) {
		output.writeInt(object.getX());
		output.writeInt(object.getY());
		kryo.writeClassAndObject(output, object.getMaterial());
	}

	@Override
	public Field read(final Kryo kryo, final Input input, final Class<Field> type) {
		int x = input.readInt();
		int y = input.readInt();
		Material material = (Material) kryo.readClassAndObject(input);
		
		return new Field(x, y, material);
	}

}
