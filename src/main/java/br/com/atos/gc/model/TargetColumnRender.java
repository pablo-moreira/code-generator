/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atos.gc.model;

/**
 *
 * @author pablo-moreira
 */
public class TargetColumnRender {
	
	private boolean renderRenderColumn;
	private boolean renderRenderFilter;
	private boolean renderRenderForm;
	private boolean renderAttributeDescription;
	private boolean renderFormType;

	public TargetColumnRender(boolean renderRenderColumn, boolean renderRenderFilter, boolean renderRenderForm, boolean renderAttributeDescription, boolean renderFormType) {
		this.renderRenderColumn = renderRenderColumn;
		this.renderRenderFilter = renderRenderFilter;
		this.renderRenderForm = renderRenderForm;
		this.renderAttributeDescription = renderAttributeDescription;
		this.renderFormType = renderFormType;
	}
	
	public boolean isRenderRenderColumn() {
		return renderRenderColumn;
	}

	public boolean isRenderRenderFilter() {
		return renderRenderFilter;
	}

	public boolean isRenderRenderForm() {
		return renderRenderForm;
	}

	public boolean isRenderAttributeDescription() {
		return renderAttributeDescription;
	}

	public boolean isRenderFormType() {
		return renderFormType;
	}	
}
