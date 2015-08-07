package com.github.cg.model;

import java.util.HashMap;

import com.github.cg.task.Task;

public class TargetTask {

	private Class<? extends Task> task;
	private HashMap<String,String> configs = new HashMap<String,String>();
	
	public TargetTask() {}

	public TargetTask(Class<? extends Task> task) {
		this.task = task;
	}

	public Class<? extends Task> getTask() {
		return task;
	}

	public void setTask(Class<? extends Task> task) {
		this.task = task;
	}

	public HashMap<String, String> getConfigs() {
		return configs;
	}

	public void setConfigs(HashMap<String, String> configs) {
		this.configs = configs;
	}
	
	public String getConfigValueAsString(String key) {
		return getConfigs().get(key);
	}
	
	public Long getConfigValueAsLong(String key) {
		return Long.valueOf(getConfigs().get(key));
	}
	
	public Integer getConfigValueAsInteger(String key) {
		return Integer.valueOf(getConfigs().get(key));
	}
	
	public Boolean getConfigValueAsBoolean(String key) {
		return Boolean.valueOf(getConfigs().get(key));
	}
}