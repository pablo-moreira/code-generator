package com.github.cg.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AccessType;

import com.github.cg.component.StringUtils;
import com.github.cg.util.JpaReflectionUtils;
import com.github.cg.util.ReflectionUtils;

public class Entity {

	private final Class<?> entityClass;
	private Gender gender;
	private String label;
	private String plural;
	private AccessType accessType;
	private final List<Attribute> attributes = new ArrayList<Attribute>();
	
	private String attributeDescription;
    private Field attributeDescriptionField;
	private Method attributeDescriptionProperty;
		
	public Entity(Class<?> entityClass) {		
		this.entityClass = entityClass;
	}
			
	public List<Attribute> getAttributes() {
		return attributes;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}
	
	public Gender getGender() {
		return gender;
	}
        
    public String getGenderDescription() {
        return gender.getDescription();
    }

	public String getLabel() {
		return label;
	}
	
	public String getLabelFlc() {
		return StringUtils.getInstance().firstToLowerCase(getLabel());
	}
	
	/**
	 * @return O nome da classe da entidade 
	 */
	public String getName() {
		return getEntityClass().getSimpleName();
	}
	
	/**
	 * @return O nome da classe da entidade com a primeira letra minuscula
	 */
	public String getNameFlc() {
		return StringUtils.getInstance().firstToLowerCase(getName());
	}
	
	public String getPackage() {
		return getEntityClass().getPackage().getName();
	}
	
	public List<AttributeManyToOne> getAttributesManyToOne() {
		
		List<AttributeManyToOne> attributesManyToOne = new ArrayList<AttributeManyToOne>();
		
		for (Attribute attribute : getAttributes()) {
			if (attribute instanceof AttributeManyToOne) {
				attributesManyToOne.add((AttributeManyToOne) attribute);
			}
		}
		
		return attributesManyToOne;	
	}
	
	public List<AttributeOneToMany> getAttributesOneToMany() {
		
		List<AttributeOneToMany> attributesOneToMany = new ArrayList<AttributeOneToMany>();
		
		for (Attribute attribute : getAttributes()) {
			if (attribute instanceof AttributeOneToMany) {
				attributesOneToMany.add((AttributeOneToMany) attribute);
			}
		}
		
		return attributesOneToMany;	
	}
	
	public AttributeId getAttributeId() {
		
		for (Attribute attribute : getAttributes()) {
			if (attribute instanceof AttributeId) {
				return (AttributeId) attribute;
			}
		}
		
		return null;
	}
	
	public String getAttributeIdClass() {
		return getAttributeId().getType().getSimpleName();
	}
	
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLabelDefault() {
		this.label = getName();
	}
	
	public boolean isHaveAttributeOneToMany() {
		return getAttributesOneToMany().size() > 0;
	}

	public List<Attribute> getAttributesWithoutAttributesOneToMany() {

		List<Attribute> attributes = new ArrayList<Attribute>();
		
		for (Attribute attr : getAttributes()) {
			
			if (!(attr instanceof AttributeOneToMany)) { 
				attributes.add(attr);
			}
		}
		
		return attributes;
	}
	
	public List<Attribute> getAttributesWithoutAttributesOneToManyAndIdAndVersion() {
	
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		for (Attribute attr : getAttributes()) {
			
			if (!AttributeId.class.isInstance(attr) && !AttributeOneToMany.class.isInstance(attr) && !attr.isAnnotedWithVersion()) { 
				attributes.add(attr);
			}
		}
		
		return attributes;
	}

	public AccessType getAccessType() {
		return accessType;
	}
	
	public String getPlural() {
		return plural;
	}

	public void setPluralDefault() {
		this.plural = getName() + "s";
	}
	
	/**
	 * Metodo responsavel por retornar o nome da entidade no plural sem caracteres acentuados ou especiais
	 * 
	 * @return O nome da entidade no plural para utilizar em nome de Variavel ou Metodo 
	 * 
	 */
	public String getPluralFvm() {
		return StringUtils.getInstance().formatForVariableOrMethodName(getPlural());
	}
	
	public String getPluralFlc() {
		return StringUtils.getInstance().firstToLowerCase(getPlural());
	}

	public void setPlural(String plural) {
		this.plural = plural;
	}

	public void addAtribute(Attribute attribute) {
		getAttributes().add(attribute);
	}
	
	public void setAttributeDescription(String attributeDescription) {

		this.attributeDescriptionField = null;
		this.attributeDescriptionProperty = null;

		if (!StringUtils.getInstance().isNullOrEmpty(attributeDescription)) {
			
			String[] items = attributeDescription.split("\\.");
		
			AccessType accessType = JpaReflectionUtils.determineAccessType(getEntityClass());
		
			if (AccessType.FIELD.equals(accessType)) {

				Field fieldFounded = null;
				String path = "";
				
				for (int i=0; i < items.length; i++) {
					
					String item = items[i];
					
					List<Field> fields;
					
					if (i == 0) {
						// Verifica se este atributo existe na associacao
						fields = ReflectionUtils.getFieldsRecursive(getEntityClass());
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
					this.attributeDescriptionField = fieldFounded;
				    this.attributeDescription = path;
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
						properties = JpaReflectionUtils.getPropertiesGettersRecursive(getEntityClass());
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
					this.attributeDescriptionProperty = propertyFounded;
				    this.attributeDescription = path;
				}
			}
		}
    }

	public String getAttributeDescription() {
		return attributeDescription;
	}
	
	public boolean isAttributeDescriptionAccessTypeField() {
		return attributeDescriptionField != null;
	}
	
	public <T extends Annotation> T getAnnotationOfAttributeDescription(Class<T> clazz) {
		return isAttributeDescriptionAccessTypeField() ? attributeDescriptionField.getAnnotation(clazz) : attributeDescriptionProperty.getAnnotation(clazz);
	}
	
	/** 
	 * Recupera o atributo anotado com @Version
	 * @return O atributo anotado com @Version se existir e nulo caso nao exista
	 */
	public Attribute getAttributeVersion() {
		
		for (Attribute attribute : getAttributes()) {
			if (attribute.isAnnotedWithVersion()) {
				return attribute;
			}
		}
		
		return null;
	}
	
	public boolean isDeclaredAttributeVersion() {
		return getAttributeVersion() != null;
	}
}