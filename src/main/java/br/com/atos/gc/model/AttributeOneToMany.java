package br.com.atos.gc.model;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.OneToMany;

import br.com.atos.utils.StringUtils;
import br.com.atosdamidia.comuns.modelo.IBaseEntity;

public class AttributeOneToMany extends Attribute {
	
	private AttributeFormType formType = AttributeFormType.EXTERNAL;

	private Entity associationEntity;

	public AttributeOneToMany() {}
        
	public AttributeOneToMany(Field field, Entity entity) {
		
		super(field, entity);
		
		load();
				
		if (!IBaseEntity.class.isAssignableFrom(getAssociationClass())) {
			throw new RuntimeException("A classe da associação " + getField().getName() + " não implementa a interface IBaseEntity!");
		}
	}	
	
	public void initializeAssociationEntity() {

		if (IBaseEntity.class.isAssignableFrom(getAssociationClass())) {
			
			@SuppressWarnings("unchecked")
			Class<? extends IBaseEntity<?>> clazz = (Class<? extends IBaseEntity<?>>) getAssociationClass();
			
			associationEntity = new Entity(clazz, getEntity().getGc());			
		}
		else {
			throw new RuntimeException("A classe da associação " + getField().getName() + " não implementa a interface IBaseEntity!");
		}
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
		
		Type type = getField().getGenericType();
		
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

		return getField().getType().getName();
	}

	public String getAssociationMappedBy() {
		
		String mappedBy = "";
		
		if (getField().getAnnotation(OneToMany.class) != null) {
			mappedBy = getField().getAnnotation(OneToMany.class).mappedBy();
		}
		
		return mappedBy;
	}
	
	@Override
	protected void load() {
	
		super.load();
		
		String formTypeValue = getGc().getGcProperties().getProperty(getPropertiesKeyBase() + ".formType");
		
		if (!StringUtils.isNullOrEmpty(formTypeValue)) {
			formType = AttributeFormType.valueOf(formTypeValue);
		}
	}
	
	public void store() {
		
		super.store();

		if (getFormType() != null) {
			getGc().getGcProperties().setProperty(getPropertiesKeyBase() + ".formType", getFormType().name());
		}
	}

	public Entity getAssociationEntity() {
		return associationEntity;
	}

	public List<Attribute> getAssociationAttributesWithoutAttributeMappedByAndAttributesOneToMany() {

		List<Attribute> attributes = new ArrayList<Attribute>();
		
		for (Attribute attribute : getAssociationEntity().getAttributesWithoutAttributesOneToMany()) {
			if (!attribute.getField().getName().equals(getAssociationMappedBy())) {
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