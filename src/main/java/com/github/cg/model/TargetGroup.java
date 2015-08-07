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
		this.targets = targets;
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
}