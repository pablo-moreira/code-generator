package com.github.cg.task;

import br.com.atos.cg.CodeGenerator;

import com.github.cg.model.TargetContext;
import com.github.cg.model.TargetTask;

public abstract class Task {

	private TargetContext targetContext;
	private TargetTask targetTask;
	private CodeGenerator codeGenerator;

	public void init(CodeGenerator codeGenerator, TargetContext targetContext, TargetTask targetTask) {
		this.codeGenerator = codeGenerator;
		this.targetContext = targetContext;
		this.targetTask = targetTask;		
	}
	
	public CodeGenerator getCodeGenerator() {
		return codeGenerator;
	}

	public TargetContext getTargetContext() {
		return targetContext;
	}

	public TargetTask getTargetTask() {
		return targetTask;
	}
	
	public abstract TaskResult execute();
	
}