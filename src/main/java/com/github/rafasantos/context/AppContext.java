package com.github.rafasantos.context;

public interface AppContext {

	<T> T getBean(Class<T> c);

}
