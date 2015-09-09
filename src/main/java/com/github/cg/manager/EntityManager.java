package com.github.cg.manager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.persistence.AccessType;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.com.atos.cg.util.LinkedProperties;
import br.com.atos.core.util.JpaReflectionUtils;
import br.com.atos.utils.ReflectionUtils;
import br.com.atos.utils.StringUtils;

import com.github.cg.model.Attribute;
import com.github.cg.model.AttributeFormType;
import com.github.cg.model.AttributeId;
import com.github.cg.model.AttributeManyToOne;
import com.github.cg.model.AttributeOneToMany;
import com.github.cg.model.Entity;
import com.github.cg.model.Gender;

public class EntityManager extends BaseManager {

	public EntityManager(ManagerRepository repository) {
		super(repository);
	}
	
	public Entity loadEntity(Class<?> entityClass, LinkedProperties cgProperties, LinkedProperties cgMessagesProperties) {
	
		Entity entity = new Entity(entityClass);
		
		// Verifica se possui os dados no gc.properties
		String genderValue = cgProperties.getProperty(entityClass.getName() + ".gender");

		if (!StringUtils.isNullOrEmpty(genderValue)) {
			entity.setGender(Gender.valueOf(genderValue));
		}
		
		String labelValue = cgMessagesProperties.getProperty(entityClass.getName());
		
		if (!StringUtils.isNullOrEmpty(labelValue)) {
			entity.setLabel(labelValue);
		}

		String pluralValue = cgMessagesProperties.getProperty(entityClass.getName() + "-plural");
		
		if (!StringUtils.isNullOrEmpty(pluralValue)) {
			entity.setPlural(pluralValue);
		}
		
		loadAttributes(entity, entityClass, cgProperties, cgMessagesProperties);
		
		loadAttributesOneToMany(entity, cgProperties, cgMessagesProperties);
		
		return entity;
	}
	
	private void loadAttributesOneToMany(Entity entity,	LinkedProperties cgProperties, LinkedProperties cgMessagesProperties) {

		List<AttributeOneToMany> attributesOneToMany = entity.getAttributesOneToMany();
		
		for (AttributeOneToMany attribute : attributesOneToMany) {
			
			loadAssociationEntity(attribute);
			
			loadAttributesOneToMany(attribute.getAssociationEntity(), cgProperties, cgMessagesProperties);
		}		
	}

	private void loadAttributes(Entity entity, Class<?> entityClass, LinkedProperties cgProperties, LinkedProperties cgMessagesProperties) {

		AccessType accessType = JpaReflectionUtils.determineAccessType(entityClass);
		
		if (AccessType.FIELD.equals(accessType)) {
		
			List<Field> fields = ReflectionUtils.getFieldsRecursive(entityClass);
			
			for (Field field : fields) {
				
				if (!Modifier.isStatic(field.getModifiers())) {
				
					Attribute attribute;
					
					if (field.getAnnotation(Id.class) != null || field.getAnnotation(EmbeddedId.class) != null) {
						attribute = new AttributeId(field, entity);
					}
					else {
						if (field.getAnnotation(OneToMany.class) != null) {
							attribute = new AttributeOneToMany(field, entity);
						}
						else if (field.getAnnotation(ManyToOne.class) != null || field.getAnnotation(OneToOne.class) != null) {
							attribute = new AttributeManyToOne(field, entity);
						}
						else {
							attribute = new Attribute(field, entity);
						}
					}
					
					loadAttribute(attribute, cgProperties, cgMessagesProperties);
					
					entity.addAtribute(attribute);
				}
			}
		}
		else if (AccessType.PROPERTY.equals(accessType)) {
			
			List<Method> propertiesGetters = JpaReflectionUtils.getPropertiesGettersRecursive(entityClass);
			
			for (Method propertyGetter : propertiesGetters) {
				
				Attribute attribute;
				
				if (propertyGetter.getAnnotation(Id.class) != null || propertyGetter.getAnnotation(EmbeddedId.class) != null) {
					attribute = new AttributeId(propertyGetter, entity);					
				}
				else {
					if (propertyGetter.getAnnotation(OneToMany.class) != null) {
						attribute = new AttributeOneToMany(propertyGetter, entity);
					}
					else if (propertyGetter.getAnnotation(ManyToOne.class) != null || propertyGetter.getAnnotation(OneToOne.class) != null) {
						attribute = new AttributeManyToOne(propertyGetter, entity);
					}
					else {
						attribute = new Attribute(propertyGetter, entity);
					}
				}
				
				loadAttribute(attribute, cgProperties, cgMessagesProperties);
				
				entity.addAtribute(attribute);
			}
		}	
	}

	public void writeEntity(Entity entity, LinkedProperties cgProperties, LinkedProperties messagesProperties) {
		
		String keyBase = entity.getEntityClass().getName();
		
		if (entity.getGender() != null) {
			// Verifica se possui os dados no gc.properties
			cgProperties.setProperty(keyBase + ".gender", entity.getGender().name());
		}

		if (entity.getLabel() != null) {
			messagesProperties.setProperty(keyBase, entity.getLabel());
		}
		
		if (entity.getPlural() != null) {
			messagesProperties.setProperty(keyBase + "-plural", entity.getPlural());
		}
	
		for (Attribute attribute : entity.getAttributes()) {
			writeAttribute(attribute, cgProperties, messagesProperties);
		}
	}
	
	public void writeAttribute(Attribute attribute, LinkedProperties cgProperties, LinkedProperties messagesProperties) {

		cgProperties.setProperty(attribute.getPropertiesKeyBase() + ".renderColumn", attribute.isRenderColumn().toString());
		cgProperties.setProperty(attribute.getPropertiesKeyBase() + ".renderFilter", attribute.isRenderFilter().toString());
		cgProperties.setProperty(attribute.getPropertiesKeyBase() + ".renderForm", attribute.isRenderForm().toString());
		
		if (attribute.getLabel() != null) {
			messagesProperties.setProperty(attribute.getPropertiesKeyBase(), attribute.getLabel());	
		}
		
		if (attribute instanceof AttributeOneToMany) {
			storeAttributeOneToMany((AttributeOneToMany) attribute, cgProperties, messagesProperties);
		}
		else if (attribute instanceof AttributeManyToOne) {
			storeAttributeManyToOne((AttributeManyToOne) attribute, cgProperties, messagesProperties);
		}
	}
	
	private void storeAttributeManyToOne(AttributeManyToOne attribute, LinkedProperties cgProperties, LinkedProperties messagesProperties) {
		
		if (!StringUtils.isNullOrEmpty(attribute.getDescriptionAttributeOfAssociation())) {
			cgProperties.setProperty(attribute.getPropertiesKeyBase() + ".descriptionAttributeAssociation", attribute.getDescriptionAttributeOfAssociation());
		}
	}

	private void storeAttributeOneToMany(AttributeOneToMany attribute, LinkedProperties cgProperties, LinkedProperties messagesProperties) {
		
		if (attribute.getFormType() != null) {
			cgProperties.setProperty(attribute.getPropertiesKeyBase() + ".formType", attribute.getFormType().name());
		}
	}
		
	private void loadAttribute(Attribute attribute, LinkedProperties cgProperties, LinkedProperties messagesProperties) {

		// gc.properties
		String renderColumnValue = cgProperties.getProperty(attribute.getPropertiesKeyBase() + ".renderColumn");
		String renderFilterValue = cgProperties.getProperty(attribute.getPropertiesKeyBase() + ".renderFilter");
		String renderFormValue = cgProperties.getProperty(attribute.getPropertiesKeyBase() + ".renderForm");

		// messages.properties
		String labelValue = messagesProperties.getProperty(attribute.getPropertiesKeyBase());
		
		
		if (!StringUtils.isNullOrEmpty(renderColumnValue)) {
			attribute.setRenderColumn(Boolean.valueOf(renderColumnValue));
		}
		
		if (!StringUtils.isNullOrEmpty(renderFilterValue)) {
			attribute.setRenderFilter(Boolean.valueOf(renderFilterValue));
		}
		
		if (!StringUtils.isNullOrEmpty(renderFormValue)) {
			attribute.setRenderForm(Boolean.valueOf(renderFormValue));
		}
				
		if (!StringUtils.isNullOrEmpty(labelValue)) {
			attribute.setLabel(labelValue);
		}
		
		if (attribute instanceof AttributeOneToMany) {
			loadAttributeOneToMany((AttributeOneToMany) attribute, cgProperties, messagesProperties);
		}
		else if (attribute instanceof AttributeManyToOne) {
			loadAttributeManyToOne((AttributeManyToOne) attribute, cgProperties, messagesProperties);
		}
	}

	private void loadAttributeManyToOne(AttributeManyToOne attribute, LinkedProperties cgProperties, LinkedProperties messagesProperties) {

		String daaValue = cgProperties.getProperty(attribute.getPropertiesKeyBase() + ".descriptionAttributeAssociation");
		
		if (!StringUtils.isNullOrEmpty(daaValue)) {
			attribute.setDescriptionAttributeOfAssociation(daaValue);
		}		
	}

	private void loadAttributeOneToMany(AttributeOneToMany attribute, LinkedProperties cgProperties, LinkedProperties messagesProperties) {
		
		String formTypeValue = cgProperties.getProperty(attribute.getPropertiesKeyBase() + ".formType");

		if (!StringUtils.isNullOrEmpty(formTypeValue)) {
			attribute.setFormType(AttributeFormType.valueOf(formTypeValue));
		}
	}

	public void storeEntity(Entity entity) {
		getManagerRepository().getCodeGenerator().store(entity);		
	}

	public void loadAssociationEntity(AttributeOneToMany attribute) {

		Class<?> associationClass = attribute.getAssociationClass();
		
		Entity associationEntity = loadEntity(associationClass, getManagerRepository().getCgProperties(), getManagerRepository().getMessagesProperties());
					
		attribute.initializeAssociationEntity(associationEntity);
	}
}