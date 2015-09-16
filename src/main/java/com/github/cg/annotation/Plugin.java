package com.github.cg.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Plugin {

	/**
	 * Define os targets do plugin  
	 */
	public abstract Target[] targets() default {};
	
	/**
	 * Define os targets groups do plugin 
	 */
	public abstract TargetGroup[] targetsGroups() default {};
	
	/**
	 * Define os patterns do plugin 
	 */
	public abstract String[] patterns() default {};
	
	
	/**
	 * Define uma lista de propriedades requiradas
	 * todas as propriedades definidas nesta lista
	 * deverao ser declaradas pelas as aplicacoes 
	 * que utilizarem este plugin no arquivo
	 * cg.properties
	 * 
	 * @return A lista de propriedades
	 */
	public abstract String[] requiredProperties() default {};
	
	public abstract String[] formTypes() default {};
	
}
