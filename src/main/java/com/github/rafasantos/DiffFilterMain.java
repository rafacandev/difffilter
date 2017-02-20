package com.github.rafasantos;

import com.github.rafasantos.cli.AppCliHandler;
import com.github.rafasantos.cli.CliExecutor;
import com.github.rafasantos.context.AppContext;
import com.github.rafasantos.context.AppContextEagerlyLoaded;
import com.github.rafasantos.ui.ConsoleUi;

/**
 * Main class invoked trough the base .jar file.
 * {@code arguments} are processed by {@code AppCliHandler}.
 * If {@code arguments} contain the help argument, then it will display the help information.
 * Else, it will invoke {@code CliRunner} and initialize the application.
 * @param arguments the command line options. See --help for more info.
 */
public class DiffFilterMain {
	
	private ConsoleUi ui;
	private CliExecutor cliRunner;
	
	/**
	 * Constructor which wires dependencies provided by {@code applicationContext}.
	 * @param applicationContext
	 */
	public DiffFilterMain(AppContext applicationContext) {
		ui = applicationContext.getBean(ConsoleUi.class);
		cliRunner = applicationContext.getBean(CliExecutor.class); 
	}

	/**
	 * Uses {@code AppContextEagerlyLoaded} as the application context and delegates to {@code execute()}.
	 * @param arguments
	 */
	public static void main(String[] arguments) {
		AppContext applicationContext = AppContextEagerlyLoaded.getInstance();
		DiffFilterMain mainInstance = new DiffFilterMain(applicationContext);
		mainInstance.execute(arguments, applicationContext);
	}
	
	/**
	 * Handles the {@code arguments} and delegates to {@code CliRunner}.
	 * Displays help information if {@code arguments} asks for help.
	 * Any exception messages will be printed on the console and
	 * the application will {@code System.exit(1)}.
	 * @param arguments
	 * @param applicationContext
	 */
	public void execute(String[] arguments, AppContext applicationContext) {
		try {
			AppCliHandler cli = null;
			ui.initUi();

			// Handle command line arguments
			try {
				cli = new AppCliHandler(arguments);
			} catch (Throwable t) {
				ui.printRed("Not able to start application! " + t.getMessage());
				ui.println("Exiting the application with error code [1]. Not able to process command line options.");
				System.exit(1);
			}
			// If command line is asking for help, display the help menu 
			if (cli.isHelp()) {
				ui.println(cli.getHelpText());
			} else {
				// Proceed normally
				cliRunner.execute(cli);
			}
		} catch (Exception e) {
			ui.printRed("Error running application: " + e.getMessage());
			// TODO create a debug parameter and only print the StackTrace if in debug mode.
			e.printStackTrace();
		} finally {
			ui.finalizeUi();
		}
	}
	
}
