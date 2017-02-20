package com.github.rafasantos;

import com.github.rafasantos.cli.AppCliHandler;
import com.github.rafasantos.cli.CliRunner;
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
	CliRunner cliRunner;
	
	public DiffFilterMain(AppContext applicationContext) {
		ui = applicationContext.getBean(ConsoleUi.class);
		cliRunner = applicationContext.getBean(CliRunner.class); 
	}

	public static void main(String[] arguments) {
		AppContext applicationContext = AppContextEagerlyLoaded.getInstance();
		DiffFilterMain mainInstance = new DiffFilterMain(applicationContext);
		mainInstance.run(arguments, applicationContext);
	}
	
	public void run(String[] arguments, AppContext applicationContext) {
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
				
				cliRunner.run(cli);
			}
		} catch (Exception e) {
			ui.printRed("Error running application: " + e.getMessage());
			e.printStackTrace();
		} finally {
			ui.finalizeUi();
		}
	}
	
	public ConsoleUi getUi() {
		return ui;
	}
	
}
