package com.github.TheDwoon.robots.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.gui.GameDisplay;
import com.github.TheDwoon.robots.network.KryoRegistry;
import com.github.TheDwoon.robots.server.managers.GameManager;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AIServer implements Closeable {

    private Server discoveryServer;
    private Server server;

    private Map<Connection, AI> clients;

    public AIServer(GameManager gameManager) throws IOException {
        discoveryServer = new Server();
        discoveryServer.start();
        discoveryServer.bind(32007, 32006);

        server = new Server();
        KryoRegistry.register(server.getKryo());
        ObjectSpace objectSpace = new ObjectSpace();

        clients = new HashMap<>();

        server.addListener(new Listener() {
            @Override
            public void connected(final Connection connection) {
                objectSpace.addConnection(connection);
                AI ai = objectSpace.getRemoteObject(connection, 1, AI.class);

                synchronized (clients) {
                    gameManager.spawnAi(ai);
                    clients.put(connection, ai);
                }
            }

            @Override
            public void disconnected(final Connection connection) {
                objectSpace.removeConnection(connection);

                synchronized (clients) {
                    AI ai = clients.remove(connection);
                    gameManager.despawnAi(ai);
                }
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
