package com.github.rafasantos.context;

import com.github.rafasantos.cli.CliRunner;
import com.github.rafasantos.controller.DiffController;
import com.github.rafasantos.service.DiffService;
import com.github.rafasantos.transformer.LineTransformer;
import com.github.rafasantos.ui.ConsoleUi;

/**
 * Simple context provider. The objective of this class is to provide instance of component classes.
 * Promotes dependency injection via constructors.
 * Instances provided by this class are static final.
 * 
 * If more sophisticated Inversion of Control or Dependency Injection mechanisms are required in the future,
 * then it might be useful to substitute this implementation for Spring Framework, Google Guice or similar IoC container. 
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
public class ContextProvider {

	// Do not change the order of these static fields. As some objects need to be instantiated before others.
	private static final LineTransformer lineTransformer = new LineTransformer();
	private static final DiffService diffService = new DiffService(lineTransformer);
	private static final DiffController diffController = new DiffController(diffService);
	private static final ConsoleUi consoleUi = new ConsoleUi();
	private static final CliRunner cliRunner = new CliRunner();

	/**
	 * Private constructor forcing this class to be a singleton.
	 */
	private ContextProvider(){}

	public static CliRunner getCliRunner() {
		return cliRunner;
	}

	public static ConsoleUi getConsoleUi() {
		return consoleUi;
	}

	public static DiffController getDiffController() {
		return diffController;
	}

	public static DiffService getDiffService() {
		return diffService;
	}

	public static LineTransformer getLineTransformer() {
		return lineTransformer;
	}
	
}
