package br.com.atos.gc.model;

import java.lang.reflect.Field;

public class AttributeId extends Attribute {

	public AttributeId(Field field) {
		super(field, "Id");
		setRenderGridColumn(true);
		setRenderGridFilter(true);
	}
		
	public AttributeId(Field field, Entity entity) {
			
		super(field, entity);

		if (field.getName().equals(getLabel())) {
			setLabel("Id");
		}
	}	
}
