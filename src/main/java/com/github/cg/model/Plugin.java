package com.github.cg.model;

import java.util.ArrayList;
import java.util.List;

import br.com.atos.cg.component.Component;

public class Plugin {
	
	/* TODO - Criar mecanismo de obrigar existir uma configuracao dentro dos plugins */

	private List<Target> targets = new ArrayList<Target>();
	
	private List<TargetGroup> targetsGroups = new ArrayList<TargetGroup>();
	
	private List<Component> components = new ArrayList<Component>();

	public List<Target> getTargets() {
		return targets;
	}

	public void setTargets(List<Target> targets) {
		this.targets = targets;
	}

	public List<TargetGroup> getTargetsGroups() {
		return targetsGroups;
	}

	public void setTargetsGroups(List<TargetGroup> targetsGroups) {
		this.targetsGroups = targetsGroups;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public void addComponent(Component component) {
		getComponents().add(component);		
	}

	public void addTarget(Target target) {
		getTargets().add(target);		
	}

	public void addTargetGroup(TargetGroup targetGroup) {
		getTargetsGroups().add(targetGroup);		
	}
}