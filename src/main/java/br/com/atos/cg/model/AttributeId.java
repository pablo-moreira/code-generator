package br.com.atos.cg.model;

import java.lang.reflect.Field;

import br.com.atos.utils.StringUtils;

public class AttributeId extends Attribute {

	public AttributeId(Field field, Entity entity) {
			
		super(field, entity);

		if (StringUtils.isNullOrEmpty(getLabel())) {
			setLabel("Id");
		}
	}	
}
