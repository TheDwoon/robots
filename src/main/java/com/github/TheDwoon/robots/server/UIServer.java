package com.github.TheDwoon.robots.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.EntityObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.network.KryoRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;

public class UIServer implements Closeable {

    public static final Logger log = LogManager.getLogger();

    private final RobotsServer robotsServer;
    private Server discoveryServer;
    private Server server;

    public UIServer(final RobotsServer robotsServer) throws IOException {
        this.robotsServer = robotsServer;
        discoveryServer = new Server();
        discoveryServer.start();
        discoveryServer.bind(32017, 32016);

        server = new Server();
        KryoRegistry.register(server.getKryo());
        ObjectSpace objectSpace = new ObjectSpace();

        server.addListener(new Listener() {

            @Override
            public void connected(final Connection connection) {
                objectSpace.addConnection(connection);
                final InventoryObserver inventoryObserver =
                        ObjectSpace.getRemoteObject(connection, 1, InventoryObserver.class);
                final BoardObserver boardObserver =
                        ObjectSpace.getRemoteObject(connection, 2, BoardObserver.class);
                final EntityObserver entityObserver =
                        ObjectSpace.getRemoteObject(connection, 3, EntityObserver.class);

                robotsServer.getBoardBroadcaster().registerObserver(boardObserver);
                robotsServer.getEntityBroadcaster().registerObserver(entityObserver);
                robotsServer.getInventoryBroadcaster().registerObserver(inventoryObserver);

                new Thread(() -> robotsServer.transmitAll(boardObserver, entityObserver, inventoryObserver)).start();
            }

            @Override
            public void disconnected(final Connection connection) {
                objectSpace.removeConnection(connection);
            }
        });
        server.start();
        server.bind(32015);
    }

    @Override
    public void close() throws IOException {
        discoveryServer.stop();
        server.stop();
    }

}
