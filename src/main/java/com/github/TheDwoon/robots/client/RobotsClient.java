package com.github.TheDwoon.robots.client;

import java.io.IOException;
import java.util.Arrays;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.network.KryoRegistry;

public final class RobotsClient {
	public static void main(final String[] args) throws IOException {
		System.out.println("Client!");

		RobotsClient robotsClient = new RobotsClient();
		UpdateHandler updateHandler = robotsClient.new UpdateHandlerImpl();

		Client client = new Client();

		Kryo kryo = client.getKryo();
		ObjectSpace.registerClasses(kryo);
		KryoRegistry.register(kryo);
		ObjectSpace objectSpace = new ObjectSpace();
		objectSpace.register(1, updateHandler);

		client.start();
		client.connect(5000, "pick-your-axe.de", 32005);

		System.out.println("Going to sleep");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		client.stop();
	}

	private Board board;

	public RobotsClient() {
		board = new Board();
	}

	private class UpdateHandlerImpl implements UpdateHandler {

		@Override
		public void handleUpdate(final int[] updatedFields) {
			// TODO (sigmarw, 23.04.2017): remove dummy & implement logic
			System.out.println("Got it!" + Arrays.toString(updatedFields));
		}

	}

}
