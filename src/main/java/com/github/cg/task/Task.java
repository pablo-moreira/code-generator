package com.github.cg.task;

import com.github.cg.manager.ManagerRepository;
import com.github.cg.model.TargetContext;
import com.github.cg.model.TargetTask;

public abstract class Task {

	private TargetContext targetContext;
	private TargetTask targetTask;
	private ManagerRepository managerRepository;

	public void init(ManagerRepository repository, TargetContext targetContext, TargetTask targetTask) {
		this.managerRepository = repository;
		this.targetContext = targetContext;
		this.targetTask = targetTask;
	}
	
	public ManagerRepository getManagerRepository() {
		return managerRepository;
	}

	public TargetContext getTargetContext() {
		return targetContext;
	}

	public TargetTask getTargetTask() {
		return targetTask;
	}
	
	public abstract TaskResult execute();
	
}