package com.github.rafasantos;

import com.github.rafasantos.cli.AppCliHandler;
import com.github.rafasantos.cli.CliRunner;
import com.github.rafasantos.context.ContextProvider;
import com.github.rafasantos.ui.ConsoleUi;

/**
 * Main class invoked trough the base .jar file.
 * {@code arguments} are processed by {@code AppCliHandler}.
 * If {@code arguments} contain the help argument, then it will display the help information.
 * Else, it will invoke {@code CliRunner} and initialize the application.
 * @param arguments the command line options. See --help for more info.
 */
public class DiffFilterMain {
	
	private static ConsoleUi ui = new ConsoleUi();
	
	public static void main(String[] arguments) {
		DiffFilterMain mainInstance = new DiffFilterMain();
		mainInstance.run(arguments);
	}
	
	public void run(String[] arguments) {
		try {
			AppCliHandler cli = null;
			ui.initUi();

			// Handle command line arguments
			try {
				cli = new AppCliHandler(arguments);
			} catch (Throwable t) {
				ui.printRed("Not able to start application! " + t.getMessage());
				ui.print("Exiting the application with error code [1]. Not able to process command line options.");
				System.exit(1);
			}
			
			// If command line is asking for help, display the help menu 
			if (cli.isHelp()) {
				ui.print(cli.getHelpText());
			} else {
				// Proceed normally
				CliRunner cliRunner = ContextProvider.getCliRunner();
				cliRunner.run(cli);
			}
		} catch (Exception e) {
			ui.printRed("Error running application: " + e.getMessage());
			e.printStackTrace();
		} finally {
			ui.finalizeUi();
		}
	}
	
	public static void setUi(ConsoleUi consoleUi) {
		ui = consoleUi;
	}
}
