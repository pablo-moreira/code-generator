package br.com.atos.gc.model;

import java.io.File;

public class Target {

	private String resource;	
	private String type;	
	private boolean resourceStart;	
	private File destinationDirectory;	
	private boolean allowOverwrite;	
	private boolean initializeEntity;

	public Target(String resource, String type, boolean resourceStart, File destinationDirectory, boolean allowOverwrite, boolean initializeEntity) {
		this.resource = resource;
		this.type = type;
		this.resourceStart = resourceStart;
		this.destinationDirectory = destinationDirectory;
		this.allowOverwrite = allowOverwrite;
		this.initializeEntity = initializeEntity;
	}

	public String getResource() {
		return resource;
	}

	public String getType() {
		return type;
	}

	public boolean isResourceStart() {
		return resourceStart;
	}

	public File getDestinationDirectory() {
		return destinationDirectory;
	}

	public boolean isAllowOverwrite() {
		return allowOverwrite;
	}

	public boolean isInitializeEntity() {
		return initializeEntity;
	}	
}
