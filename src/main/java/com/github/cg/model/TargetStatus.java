/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cg.model;

/**
 *
 * @author pablo-moreira
 */
public class TargetStatus {
	
	private final Class<?> entityClass;
	private final Target target;	
	private boolean executed = false;

	public TargetStatus(Class<?> entityClass, Target target) {
		this.entityClass = entityClass;
		this.target = target;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public Target getTarget() {
		return target;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}
	
	public boolean isExecuted() {
		return executed;
	}	
}
