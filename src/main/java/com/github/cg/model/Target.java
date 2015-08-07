package com.github.cg.model;

import java.util.ArrayList;
import java.util.List;

public class Target {

	/**
	 * O nome do target
	 * Exemplo: DAO
	 */
	private String name;
	
	/**
	 * Descrição do que target ira fazer
	 * Exemplo: Gerar o arquivo DAO
	 */
	private String description;
	
	/** 
	 * Se verdadeiro o arquivo pode ser sobrescrito
	 */
	private boolean allowOverwrite;
	
	/**
	 * O nome do diretorio e do arquivo que sera criado. Aceita el 
	 */
	private String filenameTemplate;
	
	/** 
	 * O nome do arquivo template que sera utilizado na execucao do target
	 */
	private String template;
	
	/**
	 * Armazena as tarefas que serao executada antes da execucao do target
	 */
	private List<TargetTask> tasksToExecuteBefore = new ArrayList<TargetTask>();
	
	/**
	 * Armazena as tarefas que serao executada apos a execucacao do target
	 */
	private List<TargetTask> tasksToExecuteAfter = new ArrayList<TargetTask>();
		
	public Target(String name, String description, boolean allowOverwrite, String filenameTemplate, String template) {
		this.name = name;
		this.description = description;
		this.allowOverwrite = allowOverwrite;
		this.filenameTemplate = filenameTemplate;
		this.template = template;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAllowOverwrite() {
		return allowOverwrite;
	}

	public void setAllowOverwrite(boolean allowOverwrite) {
		this.allowOverwrite = allowOverwrite;
	}

	public String getFilenameTemplate() {
		return filenameTemplate;
	}

	public void setFilenameTemplate(String filenameTemplate) {
		this.filenameTemplate = filenameTemplate;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	public List<TargetTask> getTasksToExecuteBefore() {
		return tasksToExecuteBefore;
	}

	public void setTasksToExecuteBefore(List<TargetTask> tasksToExecuteBefore) {
		this.tasksToExecuteBefore = tasksToExecuteBefore;
	}

	public List<TargetTask> getTasksToExecuteAfter() {
		return tasksToExecuteAfter;
	}

	public void setTasksToExecuteAfter(List<TargetTask> tasksToExecuteAfter) {
		this.tasksToExecuteAfter = tasksToExecuteAfter;
	}

	public void addTaskExecuteBefore(TargetTask targetTask) {
		getTasksToExecuteBefore().add(targetTask);		
	}
	
	public void addTaskExecuteAfter(TargetTask targetTask) {
		getTasksToExecuteAfter().add(targetTask);		
	}
}