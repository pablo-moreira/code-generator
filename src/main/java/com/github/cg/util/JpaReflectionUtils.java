package com.github.cg.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AccessType;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

public class JpaReflectionUtils {
	
	public static Field getFieldId(Class<?> clazz) {
		
		List<Field> fields = ReflectionUtils.getFieldsRecursive(clazz);
		
		for (Field field : fields) {
			if (field.getAnnotation(Id.class) != null || field.getAnnotation(EmbeddedId.class) != null) {
				return field;
			}
		}
		
		return null;		
	}
	
	private static Method getMethodId(Class<?> clazz) {
		
		List<Method> methods = ReflectionUtils.getMethodsRecursive(clazz);
		
		for (Method method : methods) {
			if (method.getAnnotation(Id.class) != null || method.getAnnotation(EmbeddedId.class) != null) {
				return method;
			}
		}
		
		return null;	
	}
	
	public static List<Field> getFieldsOneToMany(Class<?> clazz) {

		List<Field> fields = ReflectionUtils.getFieldsRecursive(clazz);

		List<Field> resultado = new ArrayList<Field>();
		
		for (Field field : fields) {
			if (field.getAnnotation(OneToMany.class) != null) {
				resultado.add(field);
			}
		}

		return resultado;
	}

	public static AccessType determineAccessType(Class<?> clazz) {

		Field fieldId = getFieldId(clazz);
		
		if (fieldId != null) {
			return AccessType.FIELD;
		}
				
		Method method = getMethodId(clazz);
		
		if (method != null) {
			return AccessType.PROPERTY;
		}
		
		return null;
	}
	
	public static List<Method> getPropertiesGettersRecursive(Class<?> clazz) {

		List<Method> methods = ReflectionUtils.getMethodsRecursive(clazz);
		List<Method> properties = new ArrayList<Method>();

		for (Method method : methods) {
			if (method.getAnnotation(Transient.class) == null
					&& !Modifier.isStatic(method.getModifiers())
					&& !Modifier.isVolatile(method.getModifiers())
					&& (method.getName().startsWith("get") || method.getName().startsWith("is"))) {
				properties.add(method);
			}
		}

		return properties;
	}

	public static String getFieldOrPropertyIdName(Class<?> clazz) {
		
		Field fieldId = getFieldId(clazz);
		
		if (fieldId != null) {
			return fieldId.getName();
		}
		
		Method methodId = getMethodId(clazz);
		
		if (methodId != null) {
			return methodId.getName();
		}
		
		return null;
	}
}
