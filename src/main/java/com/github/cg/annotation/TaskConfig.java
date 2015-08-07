package com.github.cg.annotation;

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
