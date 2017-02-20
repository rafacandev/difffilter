package com.github.rafasantos.context;

import java.util.HashMap;
import java.util.Map;

import com.github.rafasantos.cli.CliExecutor;
import com.github.rafasantos.controller.DiffController;
import com.github.rafasantos.service.DiffService;
import com.github.rafasantos.transformer.LineTransformer;
import com.github.rafasantos.ui.ConsoleUi;

public class AppContextEagerlyLoaded implements AppContext {

	private static final AppContextEagerlyLoaded instance = new AppContextEagerlyLoaded();
	private static Map<String, Object> beans = new HashMap<>();
	
	static {
		// Be careful when changing the order of these static fields. As some objects need to be instantiated before others.
		beans.put(LineTransformer.class.getSimpleName(), new LineTransformer());
		beans.put(DiffService.class.getSimpleName(), new DiffService(getInstance()));
		beans.put(DiffController.class.getSimpleName(), new DiffController(getInstance()));
		beans.put(ConsoleUi.class.getSimpleName(), new ConsoleUi());
		beans.put(CliExecutor.class.getSimpleName(), new CliExecutor(getInstance()) );
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
