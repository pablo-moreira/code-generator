package com.github.cg.util;

public class EnumUtils {

	public static String[] valuesAsString(Enum<?>[] values) {
		
		String[] valuesAsString = new String[values.length];
		
		int i = 0;
		
		for (Enum<?> value : values) {
			valuesAsString[i++] = value.name();
		}
		
		return valuesAsString;
	}
}
