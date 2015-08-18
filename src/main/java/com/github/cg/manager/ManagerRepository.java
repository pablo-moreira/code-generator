package com.github.cg.manager;

public class ManagerRepository {

	private EntityManager entityManager;
	private TaskManager taskManager;

	public ManagerRepository() {	
		init();
	}

	private void init() {
		entityManager = new EntityManager(this);
		taskManager = new TaskManager(this); 
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}
}
