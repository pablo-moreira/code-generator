package br.com.atos.gc.model;

import br.com.atos.utils.StringUtils;
import br.com.atosdamidia.comuns.modelo.IBaseEntity;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.persistence.OneToMany;

public class AttributeOneToMany extends Attribute {
	
	private AttributeFormType formType = AttributeFormType.EXTERNAL;

	private Entity associationEntity;

	public AttributeOneToMany() {}
        
	public AttributeOneToMany(Field field, Entity entity) {
		
		super(field, entity);
		
		load();
		
		Class<?> associationClass = getAssociationClass();
		
		if (IBaseEntity.class.isAssignableFrom(associationClass)) {
			
			@SuppressWarnings("unchecked")
			Class<? extends IBaseEntity<?>> clazz = (Class<? extends IBaseEntity<?>>) associationClass;
			
			associationEntity = new Entity(clazz, entity.getGc());	
		}
		else {
			throw new RuntimeException("A classe da associação " + field.getName() + " não implementa a interface IBaseEntity!");
		}
			
	}
	
	public AttributeOneToMany(Field atributo, String label, AttributeFormType formType) {
		super(atributo, label);
		this.formType = formType;
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
			mappedBy = StringUtils.firstToUpperCase(getField().getAnnotation(OneToMany.class).mappedBy());
		}
		
		return mappedBy;
	}
	
	public Entity getAssociationEntity() {
		return associationEntity;
	}
	
	@Override
	protected void load() {
	
		super.load();
		
		String formTypeValue = getGc().getGcProperties().getProperty(getPropertiesKeyBase() + ".formType");
		
		if (!StringUtils.isNullOrEmpty(formTypeValue)) {
			formType = AttributeFormType.valueOf(formTypeValue);
		}
	}
	
	@Override
	public void store() {
		
		super.store();

		if (getFormType() != null) {
			getGc().getGcProperties().setProperty(getPropertiesKeyBase() + ".formType", getFormType().name());
		}
	}
}