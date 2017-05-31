package com.github.TheDwoon.robots.server;

import java.util.concurrent.atomic.AtomicLong;

public final class UUIDGenerator {
	private static volatile AtomicLong uuidCounter = new AtomicLong(1);
	
	private UUIDGenerator() {
		
	}
	
	public static long obtainUUID() {
		return uuidCounter.getAndIncrement();
	}
	
	public static boolean isValid(long uuid) {
		return uuid > 0;
	}
}
