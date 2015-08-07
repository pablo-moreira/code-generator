package com.github.cg.annotation;

import com.github.cg.task.Task;

public @interface TargetTask {

	public abstract Class<? extends Task> task();
	
	public abstract TaskConfig[] configs() default {};	
}
