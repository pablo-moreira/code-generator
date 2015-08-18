package com.github.cg.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.OneToMany;

import br.com.atos.core.model.IBaseEntity;

public class AttributeOneToMany extends Attribute {
	
	private AttributeFormType formType = AttributeFormType.EXTERNAL;

	private Entity associationEntity;

	public AttributeOneToMany() {}
        
	public AttributeOneToMany(Field field, Entity entity) {		
		super(field, entity);
	}	
	
	public AttributeOneToMany(Method method, Entity entity) {
		super(method, entity);
	}

	public void initializeAssociationEntity() {
			
		@SuppressWarnings("unchecked")
		Class<? extends IBaseEntity<?>> clazz = (Class<? extends IBaseEntity<?>>) getAssociationClass();
		
		/* TODO - Refatorar para o EntityManager Initialize association entity */
//		associationEntity = new Entity(clazz, getEntity().getGc());
	}
		
	public AttributeFormType getFormType() {
		return formType;
	}

	public void setFormType(AttributeFormType formType) {
		this.formType = formType;
	}
	
	public String getAssociationClassSimpleName() {
		try {
			return getAssociationClass().getSimpleName();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public Class<?> getAssociationClass() {
		try {
			return Class.forName(getAssociationClassName());
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public String getAssociationClassName() {
		
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
	
	public boolean isAllowedFormTypeInternal() {
		
		if (associationEntity == null) {
			initializeAssociationEntity();
		}
		
		return getAssociationEntity().isHaveAttributeOneToMany();
	}
}