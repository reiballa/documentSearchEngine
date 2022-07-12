package com.rei.searchengine.entities;

/**
 * Enumeration of possible commands
 * @author rba on 11-Jul-22
 */
public enum Command {
	HELP("help"),INDEX("index"), QUERY("query"), QUIT("quit");

	private final String value;

	Command(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
