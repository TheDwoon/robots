package com.github.TheDwoon.robots.mapfile;

/**
 * Created by sigmar on 28.05.17.
 */
public class ParseException extends Exception {

	public ParseException(int line, String message) {
		super(formatMessage(line, message));
	}

	public ParseException(int line, String message, Throwable cause) {
		super(formatMessage(line, message), cause);
	}

	private static String formatMessage(int line, String message) {
		return "[" + line + "] " + message;
	}
}
