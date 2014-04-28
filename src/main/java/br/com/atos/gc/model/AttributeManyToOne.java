package br.com.atos.gc.model;

import br.com.atos.utils.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.List;


public class AttributeManyToOne extends Attribute {

    private String descriptionAttributeOfAssociation; // Remover nao serve para nada.
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
}
