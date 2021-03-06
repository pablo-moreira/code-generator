package com.github.cg.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import com.github.cg.component.StringUtils;

public class Attribute {

	protected Field field;
	protected Method propertyGetter;	
	private String label;
	private String pattern;
	
	private Boolean renderColumn = true;
	private Boolean renderFilter = true;
	private Boolean renderForm = true;
	
	private Entity entity;
	private String name;

	public Attribute() {}
    
	public Attribute(Method propertyGetter, Entity entity) {
		this.propertyGetter = propertyGetter;
		this.entity = entity;
		init();
	}
	
	public Attribute(Field field, Entity entity) {
		this.field = field;
		this.entity = entity;
		init();
	}
	
	public Attribute(Field field, String label) {
		super();
		this.field = field;
		this.label = label;
		init();
	}
	
	public boolean isAccessTypeField() {
		return field != null;
	}

	private void init() {
		if (isAccessTypeField()) {
			name = field.getName();
		}
		else {
			String aux = propertyGetter.getName();
			
			aux = aux.replace("is", "").replace("get", "");
			aux = StringUtils.getInstance().firstToLowerCase(aux);
			
			name = aux;
		}
	}
	
	public String getPropertiesKeyBase() {
		return getEntity().getEntityClass().getName() + "." + getName();
	}	
	
	public String getLabel() {
		return label;
	}
	
	public String getLabelFuc() {
		return StringUtils.getInstance().firstToUpperCase(getLabel());
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

	public String getName() {
		return name; 
	}
	
	public String getNameFuc() {
		return StringUtils.getInstance().firstToUpperCase(getName());
	}
		
	public Class<?> getType() {
		return isAccessTypeField() ? field.getType() : propertyGetter.getReturnType();
	}
	
	public String getTypeName() {
		return getType().getSimpleName();
	}
	
	public String getTypeNameFlc() {
		return StringUtils.getInstance().firstToLowerCase(getName()); 
	}
			
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		if (isAccessTypeField()) {
			return field.getAnnotation(annotationClass);
		}
		else {
			return propertyGetter.getAnnotation(annotationClass);
		}
	}

	public boolean isAnnotedWithVersion() {
		return getAnnotation(Version.class) != null;
	}

	public Boolean isRequired() {

		Boolean required = false;
		
		if (getAnnotation(Column.class) != null) {
			required = !getAnnotation(Column.class).nullable();
		}
		else if (getAnnotation(OneToOne.class) != null) {
			required = !getAnnotation(OneToOne.class).optional();
		}
		else if (getAnnotation(ManyToOne.class) != null) {
			required = !getAnnotation(ManyToOne.class).optional();
		}
		else if (getAnnotation(JoinColumn.class) != null) {
			required = !getAnnotation(JoinColumn.class).nullable();
		}
		
		return required;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}