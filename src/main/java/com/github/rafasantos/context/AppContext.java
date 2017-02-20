package com.github.rafasantos.context;

/**
 * Simple application context provider.
 * The implementations of this interface will provide instances of component classes.
 * 
 * If more sophisticated Inversion of Control or Dependency Injection mechanisms are required in the future,
 * then it might be useful to use Spring Framework, Google Guice or similar IoC container. 
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
public interface AppContext {

	<T> T getBean(Class<T> c);

}
