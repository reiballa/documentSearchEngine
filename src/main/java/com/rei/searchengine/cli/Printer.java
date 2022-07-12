package com.rei.searchengine.cli;

import com.rei.searchengine.entities.Command;

import java.util.List;
import java.util.Locale;

import static java.text.MessageFormat.format;
import static java.util.ResourceBundle.getBundle;

/**
 * Helper class to print responses in the CLI.
 * @author rba on 11-Jul-22
 */
public class Printer {


	public static void printCommands() {
		System.out.println(getBundle("messages").getString("commands"));
	}

	public static void printGreeting() {
		System.out.println(getBundle("messages").getString("greeting"));
	}

	public static void commandMissingArguments() {
		System.out.println(getBundle("messages").getString("missingArguments"));
	}

	public static void indexHelp() {
		System.out.println(getBundle("messages").getString("indexHelp"));
	}

	public static void queryHelp() {
		System.out.println(getBundle("messages").getString("queryHelp"));
	}


	public static void quitHelp() {
		System.out.println(getBundle("messages").getString("quitHelp"));
	}


	public static void help() {
		System.out.println(getBundle("messages").getString("helpHelp"));
	}

	public static void invalidArgument() {
		System.out.println(getBundle("messages").getString("invalidArgument"));
	}

	public static void queryUnsupported(String queryExpression) {
		String message = format(getBundle("messages").getString("queryUnsupported"), queryExpression);
		Printer.commandError(Command.QUERY.getValue(), message);
	}

	public static void commandUnknown(String command) {
		System.out.println(format(getBundle("messages").getString("commandUnknown"), command));
	}

	public static void indexOk(long id, long time) {
		System.out.println(format(getBundle("messages").getString("indexOk"), id, time));
	}

	public static void queryResult (List<Long> results, long duration) {
		System.out.println(format(getBundle("messages").getString("queryResult"),
				results.toString()
						.replace("[","")
						.replace("]","")
						.replace(",",""), duration));
	}

	public static void commandError(String command, String message) {
		System.out.println(format(getBundle("messages", Locale.US).getString("commandError"), command, message));
	}
}
