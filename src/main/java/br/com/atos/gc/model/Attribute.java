package br.com.atos.gc.model;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.utils.StringUtils;
import java.lang.reflect.Field;

public class Attribute {

	private Field field;	
	private String label;
	
	private boolean renderColumn = true;
	private boolean renderFilter = true;
	private boolean renderForm = true;
	
	private Entity entity;

        public Attribute() {}
        
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

	public boolean isRenderColumn() {
		return renderColumn;
	}

	public void setRenderColumn(boolean renderColumn) {
		this.renderColumn = renderColumn;
	}

	public boolean isRenderFilter() {
		return renderFilter;
	}

	public void setRenderFilter(boolean renderFilter) {
		this.renderFilter = renderFilter;
	}

	public boolean isRenderForm() {
		return renderForm;
	}

	public void setRenderForm(boolean renderForm) {
		this.renderForm = renderForm;
	}

	public Entity getEntity() {
		return entity;
	}
}
