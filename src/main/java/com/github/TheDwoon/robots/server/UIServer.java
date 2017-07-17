package com.github.TheDwoon.robots.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.game.interaction.AiObserver;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.network.KryoRegistry;
import com.github.TheDwoon.robots.server.managers.GameManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UIServer implements Closeable {

    public static final Logger log = LogManager.getLogger();

    private static ExecutorService registrationExecutor = Executors.newSingleThreadExecutor();

    private Server discoveryServer;
    private Server server;

    private Map<Connection, Client> clients;

    public UIServer(final GameManager gameManager) throws IOException {
        discoveryServer = new Server();
        discoveryServer.start();
        discoveryServer.bind(32017, 32016);

        server = new Server();
        KryoRegistry.register(server.getKryo());
        ObjectSpace objectSpace = new ObjectSpace();

        clients = new HashMap<>();

        server.addListener(new Listener() {

            @Override
            public void connected(final Connection connection) {
                objectSpace.addConnection(connection);
                final BoardObserver boardObserver =
                        ObjectSpace.getRemoteObject(connection, 1, BoardObserver.class);
                final AiObserver aiObserver =
                        ObjectSpace.getRemoteObject(connection, 2, AiObserver.class);
                final InventoryObserver inventoryObserver =
                        ObjectSpace.getRemoteObject(connection, 3, InventoryObserver.class);

                synchronized (clients) {
                    registrationExecutor.submit(() -> {
                        gameManager.addObserver(boardObserver);
                        gameManager.addObserver(aiObserver);
                        gameManager.addObserver(inventoryObserver);
                    });
                    clients.put(connection, new Client(boardObserver, aiObserver, inventoryObserver));
                }
            }

            @Override
            public void disconnected(final Connection connection) {
                objectSpace.removeConnection(connection);

                synchronized (clients) {
                    Client client = clients.remove(connection);
                    registrationExecutor.submit(() -> {
                        gameManager.removeObserver(client.boardObserver);
                        gameManager.removeObserver(client.aiObserver);
                        gameManager.removeObserver(client.inventoryObserver);
                    });
                }
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

    private final class Client {
        public final BoardObserver boardObserver;
        public final AiObserver aiObserver;
        public final InventoryObserver inventoryObserver;

        public Client(BoardObserver boardObserver, AiObserver aiObserver, InventoryObserver inventoryObserver) {
            this.boardObserver = boardObserver;
            this.aiObserver = aiObserver;
            this.inventoryObserver = inventoryObserver;
        }
    }

}
