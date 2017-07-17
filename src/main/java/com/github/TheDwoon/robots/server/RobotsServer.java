package com.github.TheDwoon.robots.server;

import com.github.TheDwoon.robots.client.student.RandomDriveAI;
import com.github.TheDwoon.robots.client.student.RepeatingAI;
import com.github.TheDwoon.robots.mapfile.MapFileParser;
import com.github.TheDwoon.robots.mapfile.ParseException;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.server.actions.NoAction;
import com.github.TheDwoon.robots.server.actions.movement.DriveForward;
import com.github.TheDwoon.robots.server.actions.movement.TurnLeft;
import com.github.TheDwoon.robots.server.actions.movement.TurnRight;
import com.github.TheDwoon.robots.server.managers.BoardManager;
import com.github.TheDwoon.robots.server.managers.GameManager;

import java.io.IOException;

public final class RobotsServer implements Runnable {

    private GameManager gameManager;

    private RobotsServer() {
        BoardManager boardManager = null;
        try {
            boardManager = MapFileParser.parseBoard(getClass().getResourceAsStream("/map/simple.map"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        gameManager = new GameManager(boardManager);

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

        try (AIServer aiServer = new AIServer(gameManager); UIServer uiServer = new UIServer(gameManager)) {
            Thread.sleep(5000);
            while (true) {
                // TODO (danielw, 30.05.2017): maybe a server "shell" here

                gameManager.makeTurn();

                Thread.sleep(5);
            }
        } catch (IOException | InterruptedException e) {
            // ignore
        }
    }
}
