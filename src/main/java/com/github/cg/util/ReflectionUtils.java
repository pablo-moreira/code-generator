package com.github.cg.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {
		
	public static List<Field> getFieldsRecursive(Class<?> clazz) {
		
		List<Field> fields = new ArrayList<Field>();
			
		for (Field field : clazz.getDeclaredFields()) {
			fields.add(field);
		}
		
		if (clazz.getSuperclass() != Object.class) {
			fields.addAll(getFieldsRecursive(clazz.getSuperclass()));
		}
				
		return fields;
	}

	public static List<Method> getMethodsRecursive(Class<?> clazz) {
		
		List<Method> methods = new ArrayList<Method>();
		
		for (Method method : clazz.getDeclaredMethods()) {
			methods.add(method);
		}
		
		Class<?> superclass = clazz.getSuperclass();
		
		if (superclass != null && superclass != Object.class) {
			methods.addAll(getMethodsRecursive(clazz.getSuperclass()));
		}
				
		return methods;
	}

	public static Object getValue(Object object, Field field) {
		
		Object fieldValue = null;
			
		try {
			field.setAccessible(true);
			fieldValue = field.get(object);
		}
		catch (Exception e) {}
		finally {
			field.setAccessible(false);
		}
		
		return fieldValue;
	}

	public static void setValue(Object object, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(object, value);
		}
		catch (Exception e) {}
		finally {
			field.setAccessible(false);
		}
	}	
}
