package com.github.rafasantos;

import com.github.rafasantos.cli.AppCliHandler;
import com.github.rafasantos.cli.CliRunner;
import com.github.rafasantos.context.ContextProvider;
import com.github.rafasantos.ui.ConsoleUi;

public class DiffApplication {
	
	/**
	 * Main class invoked trough the base .jar file.
	 * {@code arguments} are processed by {@code AppCliHandler}.
	 * If {@code arguments} contain the help argument, then it will display the help information.
	 * Else, it will invoke {@code CliRunner} and initialize the application.
	 * @param arguments the command line options. See --help for more info.
	 */
	public static void main(String[] arguments) {
		// TODO: Create an abstraction to handle Console outputs instead of use System.err or System.out
		// Handles the command line options.
		AppCliHandler cli = null;
		try {
			cli = new AppCliHandler(arguments);
		} catch (Throwable t) {
			System.err.println("Not able to start application! " + t.getMessage());
			System.err.println("Exiting the application with error code [1]. Not able to process command line options.");
			System.exit(1);
		}
		// If line output message is interrupted; then, display message 
		if (cli.isHelp()) {
			System.out.println(cli.getHelpText());
		} else {
			// Proceed normally
			ConsoleUi.initUi();
			
			CliRunner cliRunner = ContextProvider.getCliRunner();
			cliRunner.run(cli);
			
			ConsoleUi.finalizeUi();
		}
	}
}
