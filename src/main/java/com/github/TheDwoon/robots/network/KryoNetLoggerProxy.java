package com.github.TheDwoon.robots.network;

import static org.apache.logging.log4j.Level.DEBUG;
import static org.apache.logging.log4j.Level.ERROR;
import static org.apache.logging.log4j.Level.FATAL;
import static org.apache.logging.log4j.Level.INFO;
import static org.apache.logging.log4j.Level.OFF;
import static org.apache.logging.log4j.Level.TRACE;
import static org.apache.logging.log4j.Level.WARN;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.esotericsoftware.minlog.Log;

public final class KryoNetLoggerProxy extends com.esotericsoftware.minlog.Log.Logger {

	private static final Logger defaultLogger = LogManager.getLogger();
	private static final Logger kryonetLogger = LogManager.getLogger("kryonet");
	private static final Logger kryoLogger = LogManager.getLogger("kryo");

	@Override
	public void log(final int level, final String category, final String message,
		final Throwable ex) {

		Logger logger;
		if (category != null) {
			switch (category) {
			case "kryonet":
				logger = kryonetLogger;
				break;
			case "kryo":
				logger = kryoLogger;
				break;
			default:
				logger = defaultLogger;
				break;
			}
		} else {
			logger = defaultLogger;
		}

		Level log4jLevel = null;

		switch (level) {
		case 6:
			log4jLevel = OFF;
			break;
		case 5:
			log4jLevel = ERROR;
			break;
		case 4:
			log4jLevel = WARN;
			break;
		case 3:
			log4jLevel = INFO;
			break;
		case 2:
			log4jLevel = DEBUG;
			break;
		case 1:
			log4jLevel = TRACE;
			break;
		default:
			log4jLevel = FATAL;
			logger.warn("Unknown kryo log level: " + level);
			break;
		}

		logger.log(log4jLevel, message, ex);
	}

	public static void setAsKryoLogger() {
		Log.set(0);
		Log.setLogger(new KryoNetLoggerProxy());
	}
}
