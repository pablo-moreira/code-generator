package com.github.cg.manager;

import com.github.cg.CodeGenerator;
import com.github.cg.util.LinkedProperties;

public class ManagerRepository {

	private CodeGenerator codeGenerator;
	private EntityManager entityManager;
	private TargetManager targetManager;
	private TaskManager taskManager;
	private TemplateManager templateManager;
	private ComponentManager componentManager;
	private CgPropertiesManager cgPropertiesManager;
	private MessagesPropertiesManager messagesPropertiesManager;	

	public ManagerRepository(CodeGenerator codeGenerator) {		
		this.cgPropertiesManager = new CgPropertiesManager(this);
		this.codeGenerator = codeGenerator;
		this.componentManager = new ComponentManager(this);		
		this.entityManager = new EntityManager(this);
		this.messagesPropertiesManager = new MessagesPropertiesManager(this);
		this.targetManager = new TargetManager(this);
		this.taskManager = new TaskManager(this);
		this.templateManager = new TemplateManager(this);
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

	public CgPropertiesManager getCgPropertiesManager() {
		return cgPropertiesManager;
	}

	public MessagesPropertiesManager getMessagesPropertiesManager() {
		return messagesPropertiesManager;
	}
}