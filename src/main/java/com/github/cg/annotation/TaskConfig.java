package com.github.cg.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TaskConfig {

	/**
	 * Define o nome da configuracao utilizada pela task
	 */
	public abstract String name();
	
	/**
	 * Define o valor da configuracao utilizada pela task
	 */
	public abstract String value();
	
}
