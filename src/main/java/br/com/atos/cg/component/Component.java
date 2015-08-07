package br.com.atos.cg.component;

import java.text.MessageFormat;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.cg.model.Attribute;
import br.com.atos.cg.model.AttributeManyToOne;

abstract public class Component {

	private CodeGenerator cg;

	public void initialize(CodeGenerator cg) {
		this.cg = cg;
	}

	public CodeGenerator getCg() {
		return cg;
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
	
	public Class<?> getType(Attribute attribute) {
		
		if (AttributeManyToOne.class.isInstance(attribute)) {
			return ((AttributeManyToOne) attribute).getDescriptionAttributeOfAssociationType();
		}
		else {
			return attribute.getType();
		}
	}
	
	public String getValue(Attribute attribute) {		
		if (AttributeManyToOne.class.isInstance(attribute)) {
			return attribute.getName() + "." + ((AttributeManyToOne) attribute).getDescriptionAttributeOfAssociation();
		}
		else {
			return attribute.getName();
		}
	}
}