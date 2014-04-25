package br.com.atos.gc.model;

import java.lang.reflect.Field;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.utils.StringUtils;

public class Attribute {

	private Field field;	
	private String label;
	
	private boolean renderGridColumn = true;
	private boolean renderGridFilter = true;
	private boolean renderWinFrm = true;
	
	private Entity entity;

	public Attribute(Field field, Entity entity) {
		this.field = field;
		this.entity = entity;
		loadProperties();
	}

	protected GeradorCodigo getGc() {
		return getEntity().getGc();
	}
	
	protected String getPropertiesKeyBase() {
		return getEntity().getClazz().getName() + "." + getField().getName();
	}
		
	protected void loadProperties() {

		// gc.properties
		String renderGridColumnValue = getGc().getGcProperties().getProperty(getPropertiesKeyBase() + ".renderGridColumn");
		String renderGridFilterValue = getGc().getGcProperties().getProperty(getPropertiesKeyBase() + ".renderGridFilter");
		String renderWinFrmValue = getGc().getGcProperties().getProperty(getPropertiesKeyBase() + ".renderWinFrm");

		// messages.properties
		String labelValue = getGc().getMessagesProperties().getProperty(getPropertiesKeyBase());

		if (!StringUtils.isNullOrEmpty(renderGridColumnValue)) {
			renderGridColumn = Boolean.valueOf(renderGridColumnValue);
		}
		
		if (!StringUtils.isNullOrEmpty(renderGridFilterValue)) {
			renderGridFilter = Boolean.valueOf(renderGridFilterValue);
		}
		
		if (!StringUtils.isNullOrEmpty(renderWinFrmValue)) {
			renderWinFrm = Boolean.valueOf(renderWinFrmValue);
		}
				
		if (!StringUtils.isNullOrEmpty(labelValue)) {
			label = labelValue;
		}
		else {
			label = field.getName();
		}
	}

	public Attribute(Field field, String label) {
		super();
		this.field = field;
		this.label = label;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isRenderGridColumn() {
		return renderGridColumn;
	}

	public void setRenderGridColumn(boolean renderGridColumn) {
		this.renderGridColumn = renderGridColumn;
	}

	public boolean isRenderGridFilter() {
		return renderGridFilter;
	}

	public void setRenderGridFilter(boolean renderGridFilter) {
		this.renderGridFilter = renderGridFilter;
	}

	public boolean isRenderWinFrm() {
		return renderWinFrm;
	}

	public void setRenderWinFrm(boolean renderWinFrm) {
		this.renderWinFrm = renderWinFrm;
	}

	public Entity getEntity() {
		return entity;
	}
}
