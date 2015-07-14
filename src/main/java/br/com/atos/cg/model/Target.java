package br.com.atos.cg.model;

import java.io.File;
import java.text.MessageFormat;

import br.com.atos.utils.StringUtils;

public class Target {

	private String name;
	private File destinationDirectory;	
	private boolean allowOverwrite;	
	
	private TargetConfig winFrmEntity;
	private TargetConfig winFrmAttributeOneToMany;
	private String templateFileName;
	

	public Target(String name, File destDirectory, String templateFileName, boolean allowOverwrite) {
		this.name = name;
		this.destinationDirectory = destDirectory;
		this.templateFileName = templateFileName;
		this.allowOverwrite = allowOverwrite;
	}

	public Target(String name, File destDirectory, String templateFileName, boolean allowOverwrite, TargetConfig winFrmEntity, TargetConfig winFrmAttributeOneToMany) {
		this(name, destDirectory, templateFileName, allowOverwrite);
		this.winFrmEntity = winFrmEntity;
		this.winFrmAttributeOneToMany = winFrmAttributeOneToMany;
	}

	public Target(String name, File destDirectory, String templateFileName, boolean allowOverwrite, TargetConfig winFrmEntity, boolean winFrmAttributeOneToMany) {
		this(name, destDirectory, templateFileName, allowOverwrite, winFrmEntity, new TargetConfig(winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany));
	}
	
	public Target(String name, String resource, String templateFileName, String type, boolean resourceStart, File destDirectory, boolean allowOverwrite, boolean winFrmEntity, TargetConfig winFrmAttributeOneToMany) {
		this(name, destDirectory, templateFileName, allowOverwrite
				, new TargetConfig(winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity)
				, winFrmAttributeOneToMany);
	}
	
	public Target(String name, String resource, String templateFileName, String type, boolean resourceStart, File destDirectory, boolean allowOverwrite, boolean winFrmEntity, boolean winFrmAttributeOneToMany) {
		this(name, destDirectory, templateFileName, allowOverwrite 
				, new TargetConfig(winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity) 
				, new TargetConfig(winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany));
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
		
		String fileName = MessageFormat.format(getName(), entity.getClassSimpleName()); 
		
		if (getName().endsWith(".xhtml")) {
			return StringUtils.firstToLowerCase(fileName);
		}
		
		return fileName;
	}
		
	public String getTemplateFileName() {
		return templateFileName;
	}

	public String getDescription() {
		return getName().replace("{0}", ""); 
	}
}