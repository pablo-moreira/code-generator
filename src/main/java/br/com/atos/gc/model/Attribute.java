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
		String renderGridColumnValue = getGc().getGcProperties().getProperty(getPropertiesKeyBase() + ".renderGridColumn");
		String renderGridFilterValue = getGc().getGcProperties().getProperty(getPropertiesKeyBase() + ".renderGridFilter");
		String renderWinFrmValue = getGc().getGcProperties().getProperty(getPropertiesKeyBase() + ".renderWinFrm");

		// messages.properties
		String labelValue = getGc().getMessagesProperties().getProperty(getPropertiesKeyBase());

		if (!StringUtils.isNullOrEmpty(renderGridColumnValue)) {
			renderColumn = Boolean.valueOf(renderGridColumnValue);
		}
		
		if (!StringUtils.isNullOrEmpty(renderGridFilterValue)) {
			renderFilter = Boolean.valueOf(renderGridFilterValue);
		}
		
		if (!StringUtils.isNullOrEmpty(renderWinFrmValue)) {
			renderForm = Boolean.valueOf(renderWinFrmValue);
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
