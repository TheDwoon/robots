package com.github.TheDwoon.robots.server;

public final class UUIDGenerator {
	private static volatile Long uuidCounter = new Long(1);
	
	private UUIDGenerator() {
		
	}
	
	public static long obtainUUID() {
		long uuid = 0;
		synchronized(uuidCounter) {
			uuid = uuidCounter++;
		}
		
		return uuid;
	}
	
	public static boolean isValid(long uuid) {
		return uuid > 0;
	}
}
