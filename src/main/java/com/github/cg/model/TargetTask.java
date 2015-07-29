package com.github.cg.model;

import java.util.HashMap;

public class TargetTask {

	private String task;
	private HashMap<String,String> configs = new HashMap<String,String>();
	
	public TargetTask() {}

	public TargetTask(String task) {
		this.task = task;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
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