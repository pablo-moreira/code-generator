package com.github.cg.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.AccessType;

import com.github.cg.component.StringUtils;
import com.github.cg.util.JpaReflectionUtils;
import com.github.cg.util.ReflectionUtils;

public class AttributeManyToOne extends Attribute {

    private String descriptionAttributeOfAssociation;
    private Field descriptionAttributeOfAssociationField;
	private Method descriptionAttributeOfAssociationProperty;
		
    public AttributeManyToOne() {}
        
    public AttributeManyToOne(Field field, String label, String descriptionAttributeOfAssociation) {
    	super(field, label);
    	this.descriptionAttributeOfAssociation = descriptionAttributeOfAssociation;
    }

    public AttributeManyToOne(Field field, Entity entity) {
    	super(field, entity);
    }

    public AttributeManyToOne(Method method, Entity entity) {
    	super(method, entity);
	}

	public String getDescriptionAttributeOfAssociation() {
    	return descriptionAttributeOfAssociation;
    }
	
	public boolean isDescriptionAttributeOfAssociationAccessTypeField() {
		return descriptionAttributeOfAssociationField != null;
	}
	
	public <T extends Annotation> T getAnnotationOfDescriptionAttributeOfAssociation(Class<T> clazz) {
		return isDescriptionAttributeOfAssociationAccessTypeField() ? descriptionAttributeOfAssociationField.getAnnotation(clazz) : descriptionAttributeOfAssociationProperty.getAnnotation(clazz);
	}

	public void setDescriptionAttributeOfAssociation(String descriptionAttributeOfAssociation) {

		this.descriptionAttributeOfAssociationField = null;
		this.descriptionAttributeOfAssociation = null;

		if (!StringUtils.getInstance().isNullOrEmpty(descriptionAttributeOfAssociation)) {
			
			String[] items = descriptionAttributeOfAssociation.split("\\.");
		
			AccessType accessType = JpaReflectionUtils.determineAccessType(getType());
		
			if (AccessType.FIELD.equals(accessType)) {

				Field fieldFounded = null;
				String path = "";
				
				for (int i=0; i < items.length; i++) {
					
					String item = items[i];
					
					List<Field> fields;
					
					if (i == 0) {
						// Verifica se este atributo existe na associacao
						fields = ReflectionUtils.getFieldsRecursive(getType());
					}
					else {
						fields = ReflectionUtils.getFieldsRecursive(fieldFounded.getType());
					}
					
					fieldFounded = null;
					
					for (Field field : fields) {
						if (field.getName().equals(item)) {
							if (i==0) {
								path = field.getName();
							}
							else {
								path += "." + field.getName();
							}
							fieldFounded = field;
							break;
						}
					}
					
					if (fieldFounded == null) {
						break;
					}
				}
				
				if (fieldFounded != null) {
					this.descriptionAttributeOfAssociationField = fieldFounded;
				    this.descriptionAttributeOfAssociation = path;
				}
			}
			else {
				Method propertyFounded = null;
				String path = "";
				
				for (int i=0; i < items.length; i++) {
					
					String item = items[i];
					
					List<Method> properties;
					
					if (i == 0) {
						// Verifica se este atributo existe na associacao
						properties = JpaReflectionUtils.getPropertiesGettersRecursive(getType());
					}
					else {
						properties = JpaReflectionUtils.getPropertiesGettersRecursive(propertyFounded.getReturnType());
					}
					
					propertyFounded = null;
					
					for (Method property : properties) {
						if (property.getName().equals(item)) {
							if (i==0) {
								path = property.getName();
							}
							else {
								path += "." + property.getName();
							}
							propertyFounded = property;
							break;
						}
					}
					
					if (propertyFounded == null) {
						break;
					}
				}
				
				if (propertyFounded != null) {
					this.descriptionAttributeOfAssociationProperty = propertyFounded;
				    this.descriptionAttributeOfAssociation = path;
				}
			}
		}
    }

	public Class<?> getDescriptionAttributeOfAssociationType() {
		return isDescriptionAttributeOfAssociationAccessTypeField() ? descriptionAttributeOfAssociationField.getType() : descriptionAttributeOfAssociationProperty.getReturnType();
	}
}
