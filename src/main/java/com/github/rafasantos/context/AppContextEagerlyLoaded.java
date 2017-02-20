package com.github.rafasantos.context;

import java.util.HashMap;
import java.util.Map;

import com.github.rafasantos.cli.CliRunner;
import com.github.rafasantos.controller.DiffController;
import com.github.rafasantos.service.DiffService;
import com.github.rafasantos.transformer.LineTransformer;
import com.github.rafasantos.ui.ConsoleUi;

public class AppContextEagerlyLoaded implements AppContext {

	private static final AppContextEagerlyLoaded instance = new AppContextEagerlyLoaded();
	private static LineTransformer lineTransformer;
	private static DiffService diffService;
	private static DiffController diffController;
	private static ConsoleUi consoleUi;
	private static CliRunner cliRunner;
	private static Map<String, Object> beans = new HashMap<>();
	
	static {
		// Do not change the order of these static fields. As some objects need to be instantiated before others.
		lineTransformer = new LineTransformer();
		beans.put(lineTransformer.getClass().getSimpleName(), lineTransformer);
		
		diffService = new DiffService(lineTransformer);
		beans.put(diffService.getClass().getSimpleName(), diffService);
		
		diffController = new DiffController(diffService);
		beans.put(diffController.getClass().getSimpleName(), diffController);
		
		consoleUi = new ConsoleUi();
		beans.put(consoleUi .getClass().getSimpleName(), consoleUi );
		
		cliRunner = new CliRunner(getInstance());
		beans.put(cliRunner .getClass().getSimpleName(), cliRunner );
	}
	
	/**
	 * Eliminate public constructor forcing this class to be a singleton.
	 */
	private AppContextEagerlyLoaded(){}

	/**
	 * Return an instance of this class
	 * @return
	 */
	public static AppContext getInstance() {
		// No need for synchronized block since this instance is final
		return instance;
	}

	@Override
	public <T> T getBean(Class<T> c) {
		Object bean = beans.get(c.getSimpleName());
		if (bean == null) {
			// TODO Create better exception class
			throw new RuntimeException("Not able to find bean for class " + c.getSimpleName());
		}
		return c.cast(bean);
	}
	
	public Map<String, Object> getBeans() {
		return beans;
	}
}
