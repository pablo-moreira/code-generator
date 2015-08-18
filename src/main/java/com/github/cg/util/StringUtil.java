package com.github.cg.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


public class StringUtil {

	public String pkgToDir(String pkg) {
		return pkg.replace(".", "/");
	}
	
	public boolean isNullOrEmpty(String string) {
		return string == null || string.length() == 0;
	}
	
	public String firstToLowerCase(String str) {
		return !isNullOrEmpty(str) ? str.substring(0,1).toLowerCase() + str.substring(1) : "";
	}
	
	public String firstToUpperCase(String str) {
		return !isNullOrEmpty(str) ? str.substring(0,1).toUpperCase() + str.substring(1) : "";
	}
	
	public String join(String[] itens, String delimiter) {
		return join(Arrays.asList(itens), delimiter);	
	}
	
	public String join(Collection<String> itens, String delimiter) {
		
		if (itens == null || itens.isEmpty()) return "";
		
		Iterator<String> iter = itens.iterator();
		
		StringBuilder builder = new StringBuilder(iter.next());

		while(iter.hasNext()) {
			builder.append(delimiter).append(iter.next());
		}
		
		return builder.toString();
	}
}