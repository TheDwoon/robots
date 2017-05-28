package com.github.TheDwoon.robots.server;

import java.io.Closeable;
import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Material;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.game.interaction.EntityObserver;
import com.github.TheDwoon.robots.mapfile.MapFileParser;
import com.github.TheDwoon.robots.mapfile.ParseException;
import com.github.TheDwoon.robots.network.KryoRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UIServer implements Closeable {

    public static final Logger log = LogManager.getLogger();

    private Server discoveryServer;
    private Server server;

    public UIServer() throws IOException {
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
                InventoryObserver inventoryObserver =
                        ObjectSpace.getRemoteObject(connection, 1, InventoryObserver.class);
                BoardObserver boardObserver =
                        ObjectSpace.getRemoteObject(connection, 2, BoardObserver.class);
                EntityObserver entityObserver =
                        ObjectSpace.getRemoteObject(connection, 3, EntityObserver.class);
                // TODO (sigmar, 26.05.2017): insert observer registration here
                new Thread(() -> {
                    try {
                        new MapFileParser(boardObserver).parse(getClass().getResourceAsStream("/map/simple.map"));
                    } catch (ParseException e) {
                        log.catching(e);
                    }
                }).start();
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
