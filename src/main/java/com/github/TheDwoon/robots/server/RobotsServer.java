package com.github.TheDwoon.robots.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.kryonet.rmi.RemoteObject;
import com.github.TheDwoon.robots.client.UpdateHandler;
import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.network.KryoRegistry;

public final class RobotsServer {
	public static void main(final String[] args) throws IOException {
		KryoNetLoggerProxy.setAsKryoLogger();

		RobotsServer robotsServer = new RobotsServer();

		Server server = new Server();
		KryoRegistry.register(server.getKryo());
		ObjectSpace objectSpace = new ObjectSpace();

		server.addListener(new Listener() {
			@Override
			public void connected(final Connection connection) {
				objectSpace.addConnection(connection);
				UpdateHandler updateHandler =
					ObjectSpace.getRemoteObject(connection, 1, UpdateHandler.class);
				robotsServer.addUpdateHandler(updateHandler);

				// TODO (sigmarw, 23.04.2017): remove test
				new Thread(() -> {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					((RemoteObject) updateHandler).setTransmitReturnValue(false);
					updateHandler.handleUpdates(new Field[] {});
					// updateHandler.handleUpdate(new Field[] { new Field(1, 1, null, true, null)
					// });
				}).start();
			}

			@Override
			public void disconnected(final Connection connection) {
				objectSpace.removeConnection(connection);
			}
		});
		server.start();
		server.bind(32005, 32006);

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
