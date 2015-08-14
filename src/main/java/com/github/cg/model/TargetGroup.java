package com.github.cg.model;

import java.util.ArrayList;
import java.util.List;


public class TargetGroup {

	private String name;	
	private List<Target> targets;

	public TargetGroup() {
		this.targets = new ArrayList<Target>();
	}
	
	public TargetGroup(String name, List<Target> targets) {
		
		this.name = name;
		this.targets = new ArrayList<Target>();
		
		for (Target target : targets) {
			addTarget(target);
		}
	}

	public void addTarget(Target target) {
		getTargets().add(target);
		target.addGroupMemberOf(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Target> getTargets() {
		return targets;
	}

	public void setTargets(List<Target> targets) {
		this.targets = targets;
	}

	public String getTargetsNames() {
		
		StringBuilder str = new StringBuilder();
		
		boolean first = true;
		
		for (Target target : getTargets()) {
			
			if (!first) {
				str.append(", ");				
			}
			else {
				first = false;
			}
			
			str.append(target.getName());
		}
		
		return str.toString();
	}
}