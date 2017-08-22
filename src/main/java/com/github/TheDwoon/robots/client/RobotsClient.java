package com.github.TheDwoon.robots.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.TheDwoon.robots.client.student.RandomItemCollectorAI;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;
import com.github.TheDwoon.robots.network.KryoRegistry;
import com.github.TheDwoon.robots.game.AI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;

public final class RobotsClient {

	private static final Logger log = LogManager.getLogger();

	private static final AI MY_AI = new RandomItemCollectorAI();
	private static boolean running;

	public static void main(final String[] args) throws IOException {
		KryoNetLoggerProxy.setAsKryoLogger();

		Client client = new Client();
		KryoRegistry.register(client.getKryo());
		ObjectSpace objectSpace = new ObjectSpace();
		objectSpace.register(1, MY_AI);
		objectSpace.addConnection(client);

		client.start();

		InetAddress serverAddress = client.discoverHost(32006, 5000);
		if (serverAddress == null) {
			serverAddress = InetAddress.getLoopbackAddress();
			log.debug("Server discovery failed. Falling back to " + serverAddress.getHostName());
		}
		client.connect(5000, serverAddress, 32005);

		running = true;
		Object lock = new Object();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			synchronized (lock) {
				running = false;
				lock.notifyAll();
			}
		}));
		try {
			synchronized (lock) {
				while (running) {
					lock.wait();
				}
			}
		} catch (InterruptedException e) {
			log.catching(e);
		}

		client.stop();
	}

}
