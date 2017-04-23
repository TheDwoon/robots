package com.github.TheDwoon.robots.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.client.UpdateHandler;
import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.network.KryoRegistry;

public final class RobotsServer {
	public static void main(final String[] args) throws IOException {
		System.out.println("Server!");

		RobotsServer robotsServer = new RobotsServer();

		Server server = new Server();
		Kryo kryo = server.getKryo();

		ObjectSpace.registerClasses(kryo);
		KryoRegistry.register(kryo);
		ObjectSpace objectSpace = new ObjectSpace();

		server.addListener(new Listener() {
			@Override
			public void connected(final Connection connection) {
				objectSpace.addConnection(connection);
				UpdateHandler updateHandler =
					ObjectSpace.getRemoteObject(connection, 1, UpdateHandler.class);
				robotsServer.addUpdateHandler(updateHandler);


				// TODO (sigmarw, 23.04.2017): remove test
				new Thread(() -> updateHandler.handleUpdate(new int[] {})).start();
				// updateHandler.handleUpdate(new Field[] { new Field(1, 1, null, true, null) });
				System.out.println("Client connected!");
			}

			@Override
			public void disconnected(final Connection connection) {
				objectSpace.removeConnection(connection);
			}
		});
		server.start();
		server.bind(32005);

		while (true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		server.stop();
	}

	private final Board board;

	private final List<UpdateHandler> updateHandlers;

	public RobotsServer() {
		board = new Board();
		updateHandlers = new LinkedList<>();
	}

	public void addUpdateHandler(final UpdateHandler updateHandler) {
		synchronized (updateHandlers) {
			updateHandlers.add(updateHandler);
		}
	}

}
