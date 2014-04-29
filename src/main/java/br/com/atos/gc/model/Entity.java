package br.com.atos.gc.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.utils.ReflectionUtils;
import br.com.atos.utils.StringUtils;
import br.com.atosdamidia.comuns.modelo.IBaseEntity;

public class Entity {

	private Class<? extends IBaseEntity<?>> clazz;
	private Gender gender;
	private String label;
	private GeradorCodigo gc;
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
	public Entity(Class<? extends IBaseEntity<?>> clazz, GeradorCodigo gc) {
		
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
	
	public GeradorCodigo getGc() {
		return gc;
	}
	
	public boolean isAttributeIgnored(String attributeName) {
		for (String attributeIgnored : getGc().getAtributosIgnorados()) {
			if (attributeName.equals(attributeIgnored)) {
				return true;
			}
		}
		return false;
	}
	
	private void initializeAttributes() {

		List<Field> fields = ReflectionUtils.getFieldsRecursive(getClazz());

		for (Field field : fields) {
	
			if (!Modifier.isStatic(field.getModifiers()) && !isAttributeIgnored(field.getName())) {
									
				if (field.getAnnotation(Id.class) != null) {
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
		
	public String getClazzSimpleName() {
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
		this.label = getClazzSimpleName();
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
}