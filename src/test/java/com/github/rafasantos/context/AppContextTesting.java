package com.github.rafasantos.context;

import java.util.HashMap;
import java.util.Map;

import com.github.rafasantos.cli.CliExecutor;
import com.github.rafasantos.ui.ConsoleUi;
import com.github.rafasantos.ui.MockConsoleUi;

public class AppContextTesting implements AppContext {
	private  Map<String, Object> beans = new HashMap<>();
	
	public AppContextTesting(){
		AppContextEagerlyLoaded acEagerlyLoaded = (AppContextEagerlyLoaded) AppContextEagerlyLoaded.getInstance();
		beans = acEagerlyLoaded.getBeans();
		// Using MockConsoleUi instead of ConsoleUi
		beans.put(ConsoleUi.class.getSimpleName(), new MockConsoleUi());
		// Re-register CliRunner as it depends on ConsoleUi
		beans.put(CliExecutor.class.getSimpleName(), new CliExecutor(this));
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
	
}
