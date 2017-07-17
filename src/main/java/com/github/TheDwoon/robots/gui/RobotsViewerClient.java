package com.github.TheDwoon.robots.gui;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.game.interaction.AiObserver;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.network.KryoRegistry;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;

public class RobotsViewerClient extends Application {

    private static final Logger log = LogManager.getLogger();

    public static void main(final String[] args) {
        launch(args);
    }

    private Client client;

    @Override
    public void start(final Stage primaryStage) throws Exception {
        KryoNetLoggerProxy.setAsKryoLogger();

        GameDisplay gameDisplay = new GameDisplay();
        Object gameDisplayFxThreadAdapter = FxThreadAdapter.create(gameDisplay, BoardObserver.class, AiObserver.class, InventoryObserver.class);

        client = new Client();
        KryoRegistry.register(client.getKryo());
        ObjectSpace objectSpace = new ObjectSpace();
        objectSpace.register(1, gameDisplayFxThreadAdapter);
        objectSpace.register(2, gameDisplayFxThreadAdapter);
        objectSpace.register(3, gameDisplayFxThreadAdapter);
        objectSpace.addConnection(client);

        client.start();


        tryConnect(32016, 32015);

//        client.addListener(new Listener() {
//            @Override
//            public void disconnected(Connection connection) {
//                new Thread(() -> tryConnect(32016, 32015)).start();
//            }
//        });

        primaryStage.setScene(new Scene(gameDisplay));
        primaryStage.setTitle("RobotsUI");
        // primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private void tryConnect(final int discoveryPort, final int tcpPort) {
        log.info("tryConnect");
        boolean connected = false;
        while (!connected) {
            try {
                InetAddress serverAddress = client.discoverHost(discoveryPort, 5000);
                if (serverAddress == null) {
                    serverAddress = InetAddress.getLoopbackAddress();
                    log.debug("Server discovery failed. Falling back to " + serverAddress.getHostName());
                }
                client.connect(5000, serverAddress, tcpPort);
                connected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() throws Exception {
        client.stop();
    }
}
