package com.github.cg.model;

import java.io.File;

import org.apache.velocity.VelocityContext;

import com.github.cg.CodeGenerator;

public class TargetContext {

	private Target target;
	private Entity entity;
	private VelocityContext context;
	private File file;
	private CodeGenerator cg;

	public TargetContext(CodeGenerator cg, Target target, Entity entity) {
		this.cg = cg;
		this.target = target;
		this.entity = entity;		
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

	public CodeGenerator getCg() {
		return cg;
	}
}