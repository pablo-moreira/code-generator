package br.com.atos.cg.model;

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

		this.descriptionAttributeOfAssociationField = null;
		this.descriptionAttributeOfAssociation = null;
    	
		if (!StringUtils.isNullOrEmpty(descriptionAttributeOfAssociation)) {
			
			String[] items = descriptionAttributeOfAssociation.split("\\.");

			Field fieldFounded = null;
			String path = "";
			
			for (int i=0; i < items.length; i++) {
			
				String item = items[i];
				
				List<Field> fields;
				
				if (i == 0) {
					// Verifica se este atributo existe na associacao
					fields = ReflectionUtils.getFieldsRecursive(getField().getType());
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
				this.descriptionAttributeOfAssociationField = fieldFounded;
			    this.descriptionAttributeOfAssociation = path;
			}
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
    
	@Override
	public void store() {
		
		super.store();

		if (!StringUtils.isNullOrEmpty(getDescriptionAttributeOfAssociation())) {
			getGc().getGcProperties().setProperty(getPropertiesKeyBase() + ".descriptionAttributeAssociation", getDescriptionAttributeOfAssociation());
		}
	}
}
