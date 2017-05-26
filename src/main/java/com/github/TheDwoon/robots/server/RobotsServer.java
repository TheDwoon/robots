package com.github.TheDwoon.robots.server;

import java.io.IOException;
import com.github.TheDwoon.robots.network.KryoNetLoggerProxy;

public final class RobotsServer {
	public static void main(final String[] args) throws IOException {
		KryoNetLoggerProxy.setAsKryoLogger();

		try (KIServer kiServer = new KIServer(); UIServer uiServer = new UIServer()) {
			while (true) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		}

	}
}
