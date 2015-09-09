package com.github.cg.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AccessType;

import org.hibernate.envers.Audited;

import com.github.cg.component.StringUtils;

public class Entity {

	private Class<?> entityClass;
	private Gender gender;
	private String label;
	private String plural;
	private AccessType accessType;
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
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
	
	public boolean isAudited() {
		return getEntityClass().isAnnotationPresent(Audited.class);
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

	public void setPlural(String plural) {
		this.plural = plural;
	}

	public void addAtribute(Attribute attribute) {
		getAttributes().add(attribute);
	}
}