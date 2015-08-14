package com.github.cg.model;

import java.io.File;

import org.apache.velocity.VelocityContext;

import br.com.atos.cg.model.Entity;

public class TargetContext {

	private Target target;
	private Entity entity;
	private VelocityContext context;
	private File file;

	public TargetContext(Target target, Entity entity) {
		this.target = target;
		this.entity = entity;		
	}
	
	public TargetContext(Target target, Entity entity, VelocityContext context) {
		this(target, entity);
		this.context = context;
	}

	public Target getTarget() {
		return target;
	}

	public Entity getEntity() {
		return entity;
	}

	public VelocityContext getContext() {
		return context;
	}

	public void setContext(VelocityContext context) {
		this.context = context;
	}

	public void setFile(File file) {
		this.file = file;		
	}

	public File getFile() {
		return file;
	}	
}