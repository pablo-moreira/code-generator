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
	
}
