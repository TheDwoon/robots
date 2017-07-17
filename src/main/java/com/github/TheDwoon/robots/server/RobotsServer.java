package com.github.TheDwoon.robots.server;

import com.esotericsoftware.kryonet.Connection;
import com.github.TheDwoon.robots.client.student.RandomDriveAI;
import com.github.TheDwoon.robots.client.student.RepeatingAI;
import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.mapfile.MapFileParser;
import com.github.TheDwoon.robots.mapfile.ParseException;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.server.actions.NoAction;
import com.github.TheDwoon.robots.server.actions.movement.DriveForward;
import com.github.TheDwoon.robots.server.actions.movement.TurnLeft;
import com.github.TheDwoon.robots.server.actions.movement.TurnRight;
import com.github.TheDwoon.robots.server.managers.GameManager;

import java.io.IOException;
import java.util.Map;

public final class RobotsServer implements Runnable {

    private GameManager gameManager;

    private Map<Connection, AI> clients;

    private RobotsServer() {
        Board board = null;
        try {
            board = MapFileParser.parseBoard(getClass().getResourceAsStream("/map/simple.map"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        gameManager = new GameManager(board);

        gameManager.spawnAi(new RandomDriveAI());
        gameManager.spawnAi(new RepeatingAI(NoAction.INSTANCE));
        gameManager.spawnAi(new RepeatingAI(TurnLeft.INSTANCE));
        gameManager.spawnAi(new RepeatingAI(TurnRight.INSTANCE));
        gameManager.spawnAi(new RepeatingAI(DriveForward.INSTANCE, TurnRight.INSTANCE));
        gameManager.spawnAi(new RepeatingAI(DriveForward.INSTANCE));
    }

    public static void main(final String[] args) throws IOException {
        RobotsServer server = new RobotsServer();
        server.run();
        System.out.println("EXIT");
    }

    @Override
    public void run() {
        KryoNetLoggerProxy.setAsKryoLogger();

        try (AIServer aiServer = new AIServer(this); UIServer uiServer = new UIServer(this)) {
            Thread.sleep(5000);
            while (true) {
                // TODO (danielw, 30.05.2017): maybe a server "shell" here

                gameManager.makeTurn();

                Thread.sleep(500);
            }
        } catch (IOException | InterruptedException e) {
            // ignore
        }
    }

    public void addClient(Connection connection, AI ai) {
        synchronized (clients) {
            gameManager.spawnAi(ai);
            clients.put(connection, ai);
        }
    }

    public void removeClient(Connection connection) {
        synchronized (clients) {
            AI ai = clients.remove(connection);
            gameManager.despawnAi(ai);
        }
    }
}
