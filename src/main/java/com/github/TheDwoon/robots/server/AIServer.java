package com.github.TheDwoon.robots.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.game.board.Field;
import com.github.TheDwoon.robots.game.entity.Robot;
import com.github.TheDwoon.robots.game.interaction.AI;
import com.github.TheDwoon.robots.game.items.Inventory;
import com.github.TheDwoon.robots.network.KryoRegistry;
import com.github.TheDwoon.robots.server.actions.PlayerAction;
import com.github.TheDwoon.robots.server.managers.GameManager;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AIServer implements Closeable {

    private final Server discoveryServer;
    private final Server server;

    private final Map<Connection, AI> clients;

    public AIServer(GameManager gameManager) throws IOException {
        this(gameManager, 32005, true);
    }

    public AIServer(GameManager gameManager, int port, boolean discoveryServerEnabled)
            throws IOException {
        if (discoveryServerEnabled) {
            discoveryServer = new Server();
            discoveryServer.start();
            discoveryServer.bind(32007, 32006);
        } else {
            discoveryServer = null;
        }

        server = new Server();
        KryoRegistry.register(server.getKryo());
        ObjectSpace objectSpace = new ObjectSpace();

        clients = new HashMap<>();

        server.addListener(new Listener() {

            @Override
            public void connected(final Connection connection) {
                objectSpace.addConnection(connection);
                AI ai = new NetworkAI(ObjectSpace.getRemoteObject(connection, 1, AI.class));

                new Thread(() -> gameManager.spawnAi(ai)).start();
                synchronized (clients) {
                    clients.put(connection, ai);
                }
            }

            @Override
            public void disconnected(final Connection connection) {
                objectSpace.removeConnection(connection);

                AI ai;
                synchronized (clients) {
                    ai = clients.remove(connection);
                }
                new Thread(() -> gameManager.despawnAi(ai)).start();
            }
        });
        server.start();
        server.bind(port);
    }

    @Override
    public void close() throws IOException {
        if (discoveryServer != null) {
            discoveryServer.stop();
        }
        server.stop();
    }

    public static final class NetworkAI implements AI {

        private static AtomicInteger counter = new AtomicInteger(0);

        private final int id;
        private final AI ai;

        public NetworkAI(AI ai) {
            this.id = counter.getAndIncrement();
            this.ai = ai;
        }

        public AI getAi() {
            return ai;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            NetworkAI networkAI = (NetworkAI) o;

            if (id != networkAI.id)
                return false;
            return ai == networkAI.ai;
        }

        @Override
        public int hashCode() {
            return id;
        }

        @Override
        public void updateRobot(Robot robot) {
            ai.updateRobot(robot);
        }

        @Override
        public void updateInventory(Inventory inventory) {
            ai.updateInventory(inventory);
        }

        @Override
        public void updateVision(List<Field> fields) {
            ai.updateVision(fields);
        }

        @Override
        public PlayerAction makeTurn() {
            return ai.makeTurn();
        }

        @Override
        public String getRobotName() {
            return ai.getRobotName();
        }
    }

}
