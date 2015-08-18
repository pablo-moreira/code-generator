package com.github.cg.manager;

public class BaseManager {

	private ManagerRepository managerRepository;

	public BaseManager(ManagerRepository repository) {
		this.managerRepository = repository;		
	}

	public ManagerRepository getManagerRepository() {
		return managerRepository;
	}	
}