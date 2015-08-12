package br.com.atos.cg.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AccessType;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.core.model.IBaseEntity;
import br.com.atos.core.util.JpaReflectionUtils;
import br.com.atos.utils.ReflectionUtils;
import br.com.atos.utils.StringUtils;

public class Entity {

	private Class<? extends IBaseEntity<?>> clazz;
	private Gender gender;
	private String label;
	private String plural;
	private CodeGenerator gc;
	private AccessType accessType;
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
	public Entity(Class<? extends IBaseEntity<?>> clazz, CodeGenerator gc) {
		
		this.clazz = clazz;
		this.gc = gc;
		
		load();
				
		initializeAttributes();
	}
		
	public List<Attribute> getAttributes() {
		return attributes;
	}

	public Class<?> getClazz() {
		return clazz;
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
	
	public CodeGenerator getGc() {
		return gc;
	}
		
	private void initializeAttributes() {

		accessType = JpaReflectionUtils.determineAccessType(getClazz());
		
		if (AccessType.FIELD.equals(accessType)) {
		
			List<Field> fields = ReflectionUtils.getFieldsRecursive(getClazz());
			
			for (Field field : fields) {
				
				if (!Modifier.isStatic(field.getModifiers())) {
										
					if (field.getAnnotation(Id.class) != null || field.getAnnotation(EmbeddedId.class) != null) {
						attributes.add(new AttributeId(field, this));
					}
					else {
						if (field.getAnnotation(OneToMany.class) != null) {
							attributes.add(new AttributeOneToMany(field, this));
						}
						else if (field.getAnnotation(ManyToOne.class) != null || field.getAnnotation(OneToOne.class) != null) {
							attributes.add(new AttributeManyToOne(field, this));
						}
						else {
							attributes.add(new Attribute(field, this));
						}
					}
				}
			}
		}
		else if (AccessType.PROPERTY.equals(accessType)) {
			
			List<Method> propertiesGetters = JpaReflectionUtils.getPropertiesGettersRecursive(getClazz());
			
			for (Method propertyGetter : propertiesGetters) {
				
				if (propertyGetter.getAnnotation(Id.class) != null || propertyGetter.getAnnotation(EmbeddedId.class) != null) {
					attributes.add(new AttributeId(propertyGetter, this));
				}
				else {
					if (propertyGetter.getAnnotation(OneToMany.class) != null) {
						attributes.add(new AttributeOneToMany(propertyGetter, this));
					}
					else if (propertyGetter.getAnnotation(ManyToOne.class) != null || propertyGetter.getAnnotation(OneToOne.class) != null) {
						attributes.add(new AttributeManyToOne(propertyGetter, this));
					}
					else {
						attributes.add(new Attribute(propertyGetter, this));
					}
				}
			}
		}	
	}
		
	public String getClassSimpleName() {
		return getClazz().getSimpleName();
	}
	
	public String getName() {
		return getClazz().getSimpleName();
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
		return getClazz().isAnnotationPresent(Audited.class);
	}
	
	private void load() {

		// Verifica se possui os dados no gc.properties
		String genderValue = gc.getGcProperties().getProperty(getClazz().getName() + ".gender");

		if (!StringUtils.isNullOrEmpty(genderValue)) {
			gender = Gender.valueOf(genderValue);
		}
		
		String labelValue = gc.getMessagesProperties().getProperty(getClazz().getName());
		
		if (!StringUtils.isNullOrEmpty(labelValue)) {
			label = labelValue;
		}
	}

	public void store() {
		
		String keyBase = getClazz().getName();
				
		if (getGender() != null) {
			// Verifica se possui os dados no gc.properties
			gc.getGcProperties().setProperty(keyBase  + ".gender", getGender().name());
		}

		if (getLabel() != null) {
			gc.getMessagesProperties().setProperty(keyBase, getLabel());
		}
	
		for (Attribute attribute : getAttributes()) {			
			attribute.store();						
		}
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLabelDefault() {
		this.label = getClassSimpleName();
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
		this.plural = getClassSimpleName() + "s";
	}

	public void setPlural(String plural) {
		this.plural = plural;
	}	
}