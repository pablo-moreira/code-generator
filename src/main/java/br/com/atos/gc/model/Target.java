package br.com.atos.gc.model;

import java.io.File;

public class Target {

	private String resource;	
	private String type;	
	private boolean resourceStart;	
	private File destinationDirectory;	
	private boolean allowOverwrite;	
	private boolean initializeEntity;	
	private TargetColumnRender frmEntity;
	private TargetColumnRender frmAttributeOneToMany;

	public Target(String resource, String type, boolean resourceStart, File destDirectory, boolean allowOverwrite, boolean initializeEntity) {
		this.resource = resource;
		this.type = type;
		this.resourceStart = resourceStart;
		this.destinationDirectory = destDirectory;
		this.allowOverwrite = allowOverwrite;
		this.initializeEntity = initializeEntity;
		this.frmEntity = new TargetColumnRender(false, false, false, false, false);
		this.frmAttributeOneToMany = new TargetColumnRender(false, false, false, false, false);
	}

	public Target(String resource, String type, boolean resourceStart, File destDirectory, boolean allowOverwrite, boolean initializeEntity, TargetColumnRender frmEntity, TargetColumnRender frmAttributeOneToMany) {
		this(resource, type, resourceStart, destDirectory, allowOverwrite, initializeEntity);
		this.frmEntity = frmEntity;
		this.frmAttributeOneToMany = frmAttributeOneToMany;
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

	public TargetColumnRender getFrmAttributeOneToMany() {
		return frmAttributeOneToMany;
	}

	public TargetColumnRender getFrmEntity() {
		return frmEntity;
	}
}