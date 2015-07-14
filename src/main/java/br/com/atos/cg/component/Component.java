package br.com.atos.cg.component;

import java.io.PrintWriter;
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
	
	abstract public String getComponentKey();
	
	abstract public void render(PrintWriter pw);
	
	protected void print(PrintWriter pw, String string) {
		pw.print(string);
	}
	
	protected void println(PrintWriter pw, String string) {
		pw.println(string);
	}
	
	protected void println(PrintWriter pw, String string, Object ... attr) {
		pw.println(MessageFormat.format(string, attr));
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