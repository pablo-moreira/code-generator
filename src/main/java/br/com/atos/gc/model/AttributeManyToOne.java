package br.com.atos.gc.model;

import java.lang.reflect.Field;
import java.util.List;

import br.com.atos.utils.ReflectionUtils;
import br.com.atos.utils.StringUtils;


public class AttributeManyToOne extends Attribute {

    private String descriptionAttributeOfAssociation;
    private Field descriptionAttributeOfAssociationField;
		
    public AttributeManyToOne() {}
        
    public AttributeManyToOne(Field field, String label, String descriptionAttributeOfAssociation) {
    	super(field, label);
    	this.descriptionAttributeOfAssociation = descriptionAttributeOfAssociation;
    }

    public AttributeManyToOne(Field field, Entity entity) {
    	super(field, entity);
    }

    public String getDescriptionAttributeOfAssociation() {
    	return descriptionAttributeOfAssociation;
    }    
	
    public Field getDescriptionAttributeOfAssociationField() {
    	return descriptionAttributeOfAssociationField;
    }

    public void setDescriptionAttributeOfAssociation(String descriptionAttributeOfAssociation) {
	
		this.descriptionAttributeOfAssociation = descriptionAttributeOfAssociation;
		
		// Verifica se este atributo existe na associacao
		List<Field> assocFields = ReflectionUtils.getFieldsRecursive(getField().getType());
	
		for (Field assocField : assocFields) {
		    if (assocField.getName().equals(descriptionAttributeOfAssociation)) {
			descriptionAttributeOfAssociationField = assocField;
			break;
		    }
		}
	
		if (descriptionAttributeOfAssociationField == null) {
		    descriptionAttributeOfAssociation = null;
		}
    }
    
	@Override
	protected void load() {
	
		super.load();
		
		String daaValue = getGc().getGcProperties().getProperty(getPropertiesKeyBase() + ".descriptionAttributeAssociation");
		
		if (!StringUtils.isNullOrEmpty(daaValue)) {
			setDescriptionAttributeOfAssociation(daaValue);
		}
	}
    
	public void store() {
		
		super.store();

		if (!StringUtils.isNullOrEmpty(getDescriptionAttributeOfAssociation())) {
			getGc().getGcProperties().add(getPropertiesKeyBase() + ".descriptionAttributeAssociation", getDescriptionAttributeOfAssociation());
		}
	}
}
