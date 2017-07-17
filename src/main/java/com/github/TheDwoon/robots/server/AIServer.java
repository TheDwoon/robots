package com.github.TheDwoon.robots.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.network.KryoRegistry;

import java.io.Closeable;
import java.io.IOException;

public class AIServer implements Closeable {

	private final RobotsServer robotsServer;
	private Server discoveryServer;
	private Server server;

	public AIServer(RobotsServer robotsServer) throws IOException {
		this.robotsServer = robotsServer;
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
				AI ai = objectSpace.getRemoteObject(connection, 1, AI.class);
				robotsServer.addClient(connection, ai);
			}

			@Override
			public void disconnected(final Connection connection) {
				objectSpace.removeConnection(connection);
				robotsServer.removeClient(connection);
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
