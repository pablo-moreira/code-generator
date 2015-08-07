package com.github.cg.annotation;


public @interface TargetGroup {

	/**
	 * Armazena o nome do target group
	 */
	public abstract String name();
	
	/**
	 * Armazena os nomes dos targes que serao executados em conjunto
	 */
	public abstract String[] targets();
	
}
