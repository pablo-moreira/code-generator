package com.github.cg.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import br.com.atos.utils.StringUtils;

public class AttributeId extends Attribute {

	public AttributeId(Field field, Entity entity) {
			
		super(field, entity);

		if (StringUtils.isNullOrEmpty(getLabel())) {
			setLabel("Id");
		}
	}

	public AttributeId(Method propertyGetter, Entity entity) {

		super(propertyGetter, entity);
		
		if (StringUtils.isNullOrEmpty(getLabel())) {
			setLabel("Id");
		}
	}
}
