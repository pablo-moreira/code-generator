package com.github.cg.manager;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.cg.util.LinkedProperties;

public class ManagerRepository {

	private CodeGenerator codeGenerator;
	private EntityManager entityManager;
	private TargetManager targetManager;
	private TaskManager taskManager;
	private TemplateManager templateManager;
	private ComponentManager componentManager;	

	public ManagerRepository(CodeGenerator codeGenerator) {		
		this.codeGenerator = codeGenerator;
		this.entityManager = new EntityManager(this);
		this.targetManager = new TargetManager(this);
		this.taskManager = new TaskManager(this);
		this.templateManager = new TemplateManager(this);
		this.componentManager = new ComponentManager(this);
	}
	
	public ComponentManager getComponentManager() {
		return componentManager;
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

	public LinkedProperties getCgProperties() {
		return getCodeGenerator().getCgProperties();
	}

	public LinkedProperties getMessagesProperties() {
		return getCodeGenerator().getMessagesProperties();
	}
}