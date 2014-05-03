package br.com.atos.gc.model;

import java.io.File;
import java.text.MessageFormat;

public class Target {

	private String resource;	
	private String type;	
	private boolean resourceStart;	
	private File destinationDirectory;	
	private boolean allowOverwrite;	
	
	private TargetConfig winFrmEntity;
	private TargetConfig winFrmAttributeOneToMany;
	private String name;

	public Target(String name, String resource, String type, boolean resourceStart, File destDirectory, boolean allowOverwrite) {
		this.name = name;
		this.resource = resource;
		this.type = type;
		this.resourceStart = resourceStart;
		this.destinationDirectory = destDirectory;
		this.allowOverwrite = allowOverwrite;
	}

	public Target(String name, String resource, String type, boolean resourceStart, File destDirectory, boolean allowOverwrite, TargetConfig winFrmEntity, TargetConfig winFrmAttributeOneToMany) {
		this(name, resource, type, resourceStart, destDirectory, allowOverwrite);
		this.winFrmEntity = winFrmEntity;
		this.winFrmAttributeOneToMany = winFrmAttributeOneToMany;
	}

	public Target(String name, String resource, String type, boolean resourceStart, File destDirectory, boolean allowOverwrite, TargetConfig winFrmEntity, boolean winFrmAttributeOneToMany) {
		this(name, resource, type, resourceStart, destDirectory, allowOverwrite, winFrmEntity, new TargetConfig(winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany));
	}
	
	public Target(String name, String resource, String type, boolean resourceStart, File destDirectory, boolean allowOverwrite, boolean winFrmEntity, TargetConfig winFrmAttributeOneToMany) {
		this(name, resource, type, resourceStart, destDirectory, allowOverwrite
				, new TargetConfig(winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity)
				, winFrmAttributeOneToMany);
	}
	
	public Target(String name, String resource, String type, boolean resourceStart, File destDirectory, boolean allowOverwrite, boolean winFrmEntity, boolean winFrmAttributeOneToMany) {
		this(name, resource, type, resourceStart, destDirectory, allowOverwrite 
				, new TargetConfig(winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity) 
				, new TargetConfig(winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany));
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

	public boolean isShowWinFrmEntity() {
		return getWinFrmEntity() != null;
	}

	public boolean isShowWinFrmAttributeOneToMany() {
		return getWinFrmAttributeOneToMany() != null;
	}
	
	public TargetConfig getWinFrmAttributeOneToMany() {
		return winFrmAttributeOneToMany;
	}

	public TargetConfig getWinFrmEntity() {
		return winFrmEntity;
	}

	public String getName() {
		return name;
	}

	public String getFileName(Entity entity) {
		return MessageFormat.format(getName(), entity.getClazzSimpleName());
	}
		
	public String getTemplateFileName() {
		return getDescription() + ".tpl";
	}

	public String getDescription() {
		return getName().replace("{0}", ""); 
	}
}