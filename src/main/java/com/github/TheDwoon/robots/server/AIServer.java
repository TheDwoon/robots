package com.github.TheDwoon.robots.server;

import java.io.Closeable;
import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.game.interaction.EntityObserver;
import com.github.TheDwoon.robots.network.KryoRegistry;

public class AIServer implements Closeable {

	private Server discoveryServer;
	private Server server;

	public AIServer() throws IOException {
		discoveryServer = new Server();
		discoveryServer.start();
		discoveryServer.bind(32007, 32006);

		server = new Server();
		KryoRegistry.register(server.getKryo());
		ObjectSpace objectSpace = new ObjectSpace();

		server.addListener(new Listener() {
			@Override
			public void connected(final Connection connection) {
				objectSpace.addConnection(connection);
				InventoryObserver inventoryObserver =
					ObjectSpace.getRemoteObject(connection, 1, InventoryObserver.class);
				BoardObserver boardObserver =
					ObjectSpace.getRemoteObject(connection, 2, BoardObserver.class);
				EntityObserver entityObserver =
					ObjectSpace.getRemoteObject(connection, 3, EntityObserver.class);
				// TODO (sigmar, 26.05.2017): insert RemoteRobot instantiation here
			}

			@Override
			public void disconnected(final Connection connection) {
				objectSpace.removeConnection(connection);
			}
		});
		server.start();
		server.bind(32005);
	}

	@Override
	public void close() throws IOException {
		discoveryServer.stop();
		server.stop();
	}

}
