package br.com.atos.gc.model;

import java.lang.reflect.Field;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.utils.StringUtils;

public class Attribute {

	private Field field;	
	private String label;
	
	private Boolean renderColumn = true;
	private Boolean renderFilter = true;
	private Boolean renderForm = true;
	
	private Entity entity;

        public Attribute() {}
        
	public Attribute(Field field, Entity entity) {
		this.field = field;
		this.entity = entity;
		load();
	}

	protected GeradorCodigo getGc() {
		return getEntity().getGc();
	}
	
	protected String getPropertiesKeyBase() {
		return getEntity().getClazz().getName() + "." + getField().getName();
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
	
	public void setLabelDefault() {
		this.label = field.getName();		
	}

	public Boolean isRenderColumn() {
		return renderColumn;
	}

	public void setRenderColumn(boolean renderColumn) {
		this.renderColumn = renderColumn;
	}

	public Boolean isRenderFilter() {
		return renderFilter;
	}

	public void setRenderFilter(boolean renderFilter) {
		this.renderFilter = renderFilter;
	}

	public Boolean isRenderForm() {
		return renderForm;
	}

	public void setRenderForm(boolean renderForm) {
		this.renderForm = renderForm;
	}

	public Entity getEntity() {
		return entity;
	}

	protected void load() {
		
		if (getGc().isIgnoredAttribute(this)) {
			renderColumn = false;
			renderFilter = false;
			renderForm = false;
		}
		
		// gc.properties
		String renderColumnValue = getGc().getGcProperties().getProperty(getPropertiesKeyBase() + ".renderColumn");
		String renderFilterValue = getGc().getGcProperties().getProperty(getPropertiesKeyBase() + ".renderFilter");
		String renderFormValue = getGc().getGcProperties().getProperty(getPropertiesKeyBase() + ".renderForm");

		// messages.properties
		String labelValue = getGc().getMessagesProperties().getProperty(getPropertiesKeyBase());
		
		if (!StringUtils.isNullOrEmpty(renderColumnValue)) {
			renderColumn = Boolean.valueOf(renderColumnValue);
		}
		
		if (!StringUtils.isNullOrEmpty(renderFilterValue)) {
			renderFilter = Boolean.valueOf(renderFilterValue);
		}
		
		if (!StringUtils.isNullOrEmpty(renderFormValue)) {
			renderForm = Boolean.valueOf(renderFormValue);
		}
				
		if (!StringUtils.isNullOrEmpty(labelValue)) {
			label = labelValue;
		}
	}
	
	public void store() {

		getGc().getGcProperties().setProperty(getPropertiesKeyBase() + ".renderColumn", isRenderColumn().toString());
		getGc().getGcProperties().setProperty(getPropertiesKeyBase() + ".renderFilter", isRenderFilter().toString());
		getGc().getGcProperties().setProperty(getPropertiesKeyBase() + ".renderForm", isRenderForm().toString());
		
		if (getLabel() != null) {
			getGc().getMessagesProperties().setProperty(getPropertiesKeyBase(), getLabel());	
		}
	}
}
