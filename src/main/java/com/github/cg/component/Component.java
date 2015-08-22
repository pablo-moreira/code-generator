package com.github.cg.component;

import java.text.MessageFormat;

import com.github.cg.model.Attribute;
import com.github.cg.model.AttributeManyToOne;
import com.github.cg.model.TargetContext;

abstract public class Component {

	private TargetContext targetContext;

	public void initialize(TargetContext targetContext) {
		this.targetContext = targetContext;
	}
			
	public TargetContext getTargetContext() {
		return targetContext;
	}

	protected void print(StringBuilder sb, String string) {
		sb.append(string);
	}
	
	protected void println(StringBuilder sb, String string) {
		sb.append(string).append("\n");
	}
	
	protected void println(StringBuilder sb, String string, Object ... attr) {
		sb.append(MessageFormat.format(string, attr)).append("\n");
	}
	
	protected String tab(int numTabs) {
		
		StringBuilder sb = new StringBuilder();
		
		for (int i=0; i<numTabs; i++) {
			sb.append("\t");
		}
		
		return sb.toString(); 
	}
	
	public Class<?> getType(Attribute attribute) {
		
		if (AttributeManyToOne.class.isInstance(attribute)) {
			return ((AttributeManyToOne) attribute).getDescriptionAttributeOfAssociationType();
		}
		else {
			return attribute.getType();
		}
	}
	
	/* TODO - Talvez retirar este metodo daqui! */
	public String getValue(Attribute attribute) {		
		if (AttributeManyToOne.class.isInstance(attribute)) {
			return attribute.getName() + "." + ((AttributeManyToOne) attribute).getDescriptionAttributeOfAssociation();
		}
		else {
			return attribute.getName();
		}
	}
}