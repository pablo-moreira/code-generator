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
	

	public Target(String name, File destDirectory, boolean allowOverwrite) {
		this.name = name;
		this.destinationDirectory = destDirectory;
		this.allowOverwrite = allowOverwrite;
	}

	public Target(String name, File destDirectory, boolean allowOverwrite, TargetConfig winFrmEntity, TargetConfig winFrmAttributeOneToMany) {
		this(name, destDirectory, allowOverwrite);
		this.winFrmEntity = winFrmEntity;
		this.winFrmAttributeOneToMany = winFrmAttributeOneToMany;
	}

	public Target(String name, File destDirectory, boolean allowOverwrite, TargetConfig winFrmEntity, boolean winFrmAttributeOneToMany) {
		this(name, destDirectory, allowOverwrite, winFrmEntity, new TargetConfig(winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany, winFrmAttributeOneToMany));
	}
	
	public Target(String name, String resource, String type, boolean resourceStart, File destDirectory, boolean allowOverwrite, boolean winFrmEntity, TargetConfig winFrmAttributeOneToMany) {
		this(name, destDirectory, allowOverwrite
				, new TargetConfig(winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity, winFrmEntity)
				, winFrmAttributeOneToMany);
	}
	
	public Target(String name, String resource, String type, boolean resourceStart, File destDirectory, boolean allowOverwrite, boolean winFrmEntity, boolean winFrmAttributeOneToMany) {
		this(name, destDirectory, allowOverwrite 
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
		
		String fileName = MessageFormat.format(getName(), entity.getClazzSimpleName()); 
		
		if (getName().endsWith(".xhtml")) {
			return StringUtils.firstToLowerCase(fileName);
		}
		
		return fileName;
	}
		
	public String getTemplateFileName() {
		return getDescription() + ".tpl";
	}

	public String getDescription() {
		return getName().replace("{0}", ""); 
	}
}