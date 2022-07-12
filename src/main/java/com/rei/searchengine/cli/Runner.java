package com.rei.searchengine.cli;

import com.rei.searchengine.entities.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * Custom Command Line Runner needed to intercept CLI inputs.
 * @author rba on 11-Jul-22
 */
@Component
public class Runner implements CommandLineRunner {

	private final Scanner scanner = new Scanner(System.in);
	private Resolver resolver;

	@Override
	public void run(String... args) throws Exception {
		Printer.printGreeting();
		Printer.printCommands();
		String input = "help";
		while (!input.equals(Command.QUIT.getValue())) {
			System.out.print("$ ");
			input = scanner.nextLine();
			resolver.resolve(input);
		}
		System.exit(0);
	}

	@Autowired
	public void setResolver(Resolver resolver) {
		this.resolver = resolver;
	}

}
