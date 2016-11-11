package com.github.cg.component;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.github.cg.annotation.Component;

@Component
public class StringUtils {

	private static StringUtils instance;
	
	public static StringUtils getInstance() {
	
		if (instance == null) {
			instance = new StringUtils();
		}
		
		return instance;
	}
	
	public String pkgToDir(String pkg) {
		return pkg.replace(".", "/");
	}
	
	public boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
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
	
	
	public String removeAllNonNumeric(String string){
		if (!isNullOrEmpty(string)) {
			return string.replaceAll("[^0-9]",	"");
		}
		return string;
	}
		
	public String stripSpecialCharacters(String string) {
		if (!isNullOrEmpty(string)) {
			return string.replaceAll("[^0-9a-zA-Z]","");
		}
		return string;
	}
						 
	public String formatForVariableOrMethodName(String string) {
		if (!isNullOrEmpty(string)) {
			return replaceAllAccents(stripSpecialCharacters(string));
		}
		return string;
	}
	
	public String replaceAllAccents(String string) {
		if (!isNullOrEmpty(string)) {
			CharSequence cs = new StringBuilder(string);  
			return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		}
		return string;
	}
}