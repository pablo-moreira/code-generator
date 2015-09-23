package com.github.cg.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.OneToMany;

import com.github.cg.component.StringUtils;

public class AttributeOneToMany extends Attribute {
	
	private String formType;

	private Entity associationEntity;

	public AttributeOneToMany() {}
        
	public AttributeOneToMany(Field field, Entity entity) {		
		super(field, entity);
	}	
	
	public AttributeOneToMany(Method method, Entity entity) {
		super(method, entity);
	}
	
	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}
	
	public String getAssociationClassNameFlc() {
		return StringUtils.getInstance().firstToLowerCase(getAssociationClassName());
	}
	
	public String getAssociationClassName() {
		return getAssociationClass().getSimpleName();
	}
	
	public Class<?> getAssociationClass() {
		try {
			return Class.forName(getAssociationClassNameByGenerics());
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public String getAssociationClassNameByGenerics() {
		
		Type type = isAccessTypeField() ? field.getGenericType() : propertyGetter.getGenericReturnType();
		
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type; 
			Type[] actualTypeArguments = pt.getActualTypeArguments();
			if (actualTypeArguments != null && actualTypeArguments.length > 0) {
				String aux = actualTypeArguments[0].toString();
				try {
					return aux.replace("class ", "");
				}
				catch (Exception e) {}
			}			
		}

		return getType().getName();
	}

	public String getAssociationMappedBy() {
		
		String mappedBy = "";
		
		if (getAnnotation(OneToMany.class) != null) {
			mappedBy = getAnnotation(OneToMany.class).mappedBy();
		}
		
		return mappedBy;
	}
	
	public String getAssociationMappedByFuc() {
		return StringUtils.getInstance().firstToUpperCase(getAssociationMappedBy());
	}

	public Entity getAssociationEntity() {
		return associationEntity;
	}

	public List<Attribute> getAssociationAttributesWithoutAttributeMappedByAndAttributesOneToMany() {

		List<Attribute> attributes = new ArrayList<Attribute>();
		
		for (Attribute attribute : getAssociationEntity().getAttributesWithoutAttributesOneToMany()) {
			if (!attribute.getName().equals(getAssociationMappedBy())) {
				attributes.add(attribute);
			}
		}
		
		return attributes;
	}
	
	public List<Attribute> getAssociationAttributesWithoutAttributeMappedByAndAttributesOneToManyAndIdAndVersion() {
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		for (Attribute attribute : getAssociationEntity().getAttributesWithoutAttributesOneToManyAndIdAndVersion()) {
			if (!attribute.getName().equals(getAssociationMappedBy())) {
				attributes.add(attribute);
			}
		}
		
		return attributes;
	}

	public void initializeAssociationEntity(Entity associationEntity) {
		this.associationEntity = associationEntity;		
	}
}