package com.github.cg.manager;

import br.com.atos.cg.CodeGenerator;

public class ManagerRepository {

	private CodeGenerator codeGenerator;
	private EntityManager entityManager;
	private TargetManager targetManager;
	private TaskManager taskManager;
	private TemplateManager templateManager;	

	public ManagerRepository(CodeGenerator codeGenerator) {		
		this.codeGenerator = codeGenerator;
		this.entityManager = new EntityManager(this);
		this.targetManager = new TargetManager(this);
		this.taskManager = new TaskManager(this);
		this.templateManager = new TemplateManager(this);
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public TargetManager getTargetManager() {
		return targetManager;
	}

	public CodeGenerator getCodeGenerator() {
		return codeGenerator;
	}

	public TemplateManager getTemplateManager() {
		return templateManager;
	}
}