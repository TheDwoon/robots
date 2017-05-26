package com.github.TheDwoon.robots.client;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.game.BoardObserver;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.InventoryObserver;
import com.github.TheDwoon.robots.game.entity.EntityObserver;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.network.KryoRegistry;

public final class RobotsClient {

	private static final Logger log = LogManager.getLogger();

	public static void main(final String[] args) throws IOException {
		KryoNetLoggerProxy.setAsKryoLogger();

		RobotsClient robotsClient = new RobotsClient();
		InventoryObserver inventoryObserver;
		BoardObserver boardObserver;
		EntityObserver entityObserver;

		Client client = new Client();
		KryoRegistry.register(client.getKryo());
		ObjectSpace objectSpace = new ObjectSpace();
		objectSpace.register(1, inventoryObserver);
		objectSpace.register(2, boardObserver);
		objectSpace.register(3, entityObserver);
		objectSpace.addConnection(client);

		client.start();

		InetAddress serverAddress = client.discoverHost(32006, 5000);
		if (serverAddress == null) {
			serverAddress = InetAddress.getLoopbackAddress();
			log.debug("Server discovery failed. Falling back to " + serverAddress.getHostName());
		}
		client.connect(5000, serverAddress, 32005);

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
		public void handleUpdates(final Field[] updates) {
			board.update(updates);
			log.debug("Board update. New board state is:%n%s", board);
		}

	}

}
