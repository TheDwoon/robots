package com.github.TheDwoon.robots.client;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.game.interaction.InventoryObserver;
import com.github.TheDwoon.robots.game.interaction.EntityObserver;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.network.KryoRegistry;

public final class RobotsClient implements Runnable {

	private static final Logger log = LogManager.getLogger();

	private RobotsClient() {
		
	}
	
	public static void main(final String[] args) throws IOException {
		KryoNetLoggerProxy.setAsKryoLogger();

		RobotsClient robotsClient = new RobotsClient();
		InventoryObserver inventoryObserver;
		BoardObserver boardObserver;
		EntityObserver entityObserver;

		Client client = new Client();
		KryoRegistry.register(client.getKryo());
		ObjectSpace objectSpace = new ObjectSpace();
		objectSpace.addConnection(client);

		client.start();

		InetAddress serverAddress = client.discoverHost(32006, 5000);
		if (serverAddress == null) {
			serverAddress = InetAddress.getLoopbackAddress();
			log.debug("Server discovery failed. Falling back to " + serverAddress.getHostName());
		}
		client.connect(5000, serverAddress, 32005);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		client.stop();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
