package com.rei.searchengine.cli;

import com.rei.searchengine.entities.Command;
import com.rei.searchengine.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class responsible for resolving CLI commands
 * @author rba on 11-Jul-22
 */
@Service
public class Resolver {

	private final SearchService searchService;

	@Autowired
	public Resolver(SearchService searchService) {
		this.searchService = searchService;
	}


	/**
	 * Entry method for resolving commands
	 *
	 * @param input String input taken from CLI
	 */
	public void resolve(String input) {
		List<String> words = Stream.of(input.split(" "))
				.map(String::trim)
				.filter(el -> !el.equals(""))
				.collect(Collectors.toList());
		String keyword = words.get(0);
		List<String> params = new ArrayList<>();
		if (words.size() > 1) {
			params = words.subList(1, words.size());
		}
		Optional<Command> command = Stream.of(Command.values())
				.filter(c -> c.getValue().equals(keyword))
				.findFirst();
		if (command.isPresent()) {
			switch (command.get()) {
				case INDEX:
					handleIndex(params);
					break;
				case QUERY:
					handleQuery(params);
					break;
				case HELP:
					handleHelp(params);
					break;
				default:
					break;
			}
		} else {
			Printer.commandUnknown(keyword);
		}
	}

	/**
	 * Handler for "help" command. Prints helpful information on all available commands.
	 *
	 * @param params List of string parameters after the command keyword
	 */
	private void handleHelp(List<String> params) {
		if (params.isEmpty()) {
			Printer.commandMissingArguments();
			Printer.printCommands();
		} else {
			String command = params.get(0);
			if (Command.INDEX.getValue().equals(command)) {
				Printer.indexHelp();
			} else if (Command.QUERY.getValue().equals(command)) {
				Printer.queryHelp();
			} else if (Command.QUIT.getValue().equals(command)) {
				Printer.quitHelp();
			} else if (Command.HELP.getValue().equals(command)) {
				Printer.help();
			} else {
				Printer.commandUnknown(command);
			}
		}
	}

	/**
	 * Handler for "query" command. Checks for query validity and prints ids of matched documents
	 * as well as the time taken to execute the query.
	 *
	 * @param params List of string parameters after the command keyword
	 */
	private void handleQuery(List<String> params) {
		// at least 1 param needed
		if (params.isEmpty()) {
			Printer.commandMissingArguments();
			return;
		}
		String queryExpression = String.join(" ", params)
				.replace("&", "AND")
				.replace("|", "OR");
		if (!Pattern.matches("[a-zA-Z0-9()\\s*]*", queryExpression)) {
			Printer.queryUnsupported(queryExpression);
			return;
		}
		searchService.query(queryExpression);
	}

	/**
	 * Handler for "index" command. Checks for command validity and saves or overrides the document if the command is valid.
	 * It prints "index ok" in case of success, as well as the time taken to save the document.
	 *
	 * @param params List of string parameters after the command keyword
	 */
	private void handleIndex(List<String> params) {
		// at least 2 params needed
		if (params.size() < 2) {
			Printer.commandMissingArguments();
			return;
		}
		// checking for validity of inputs
		if (!Pattern.matches("[0-9]+", params.get(0)) ||
				params.stream().skip(1)
						.anyMatch(arg -> !Pattern.matches("[a-zA-Z0-9]*", arg))) {
			Printer.invalidArgument();
			return;
		}

		Long documentId = Long.parseLong(params.get(0));
		List<String> tokens = params.subList(1, params.size());
		searchService.index(documentId, tokens);
	}
}
