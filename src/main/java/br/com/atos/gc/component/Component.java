package br.com.atos.gc.component;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.model.AttributeManyToOne;
import br.com.atosdamidia.comuns.modelo.BaseEnum;

abstract public class Component {

	private GeradorCodigo gc;
	
	public Component(GeradorCodigo gc) {
		this.gc = gc;
	}

	public GeradorCodigo getGc() {
		return gc;
	}
	
	abstract public String getComponenteChave();
	abstract public void renderizar(PrintWriter pw);
	
	protected void print(PrintWriter pw, String string) {
		pw.print(string);
	}
	
	protected void println(PrintWriter pw, String string) {
		pw.println(string);
	}
	
	protected void println(PrintWriter pw, String string, Object ... attr) {
		pw.println(MessageFormat.format(string, attr));
	}
	
	public Field getField(Attribute attribute) {
		
		if (AttributeManyToOne.class.isInstance(attribute)) {
			return ((AttributeManyToOne) attribute).getDescriptionAttributeOfAssociationField();
		}
		else {
			return attribute.getField();
		}
	}
	
	public String getValue(Attribute attribute) {		
		if (AttributeManyToOne.class.isInstance(attribute)) {
			return attribute.getField().getName() + "." + ((AttributeManyToOne) attribute).getDescriptionAttributeOfAssociation();
		}
		else {
			return attribute.getField().getName();
		}
	}
	
	protected void printOutputText(PrintWriter pw, String indentacao, Field field, String value) {
		
		if (BaseEnum.class.isAssignableFrom(field.getType())) {
			println(pw, "{0}<h:outputText value=\"#'{'{1}.descricao'}'\" />", indentacao, value);
		}
		else if (Date.class.isAssignableFrom(field.getType())) {
			
			println(pw, "{0}<h:outputText value=\"#'{'{1}'}'\">", indentacao, value);
			
			if (field.getAnnotation(Temporal.class).value() == TemporalType.DATE) {
				println(pw, "{0}\t<f:convertDateTime locale=\"pt_BR\" type=\"date\" />", indentacao);
			}
			else if (field.getAnnotation(Temporal.class).value() == TemporalType.TIME) {
				println(pw, "{0}\t<f:convertDateTime locale=\"pt_BR\" type=\"time\" />", indentacao);
			}
			else {
				println(pw, "{0}\t<f:convertDateTime locale=\"pt_BR\" type=\"both\" />", indentacao);
			}
			
			println(pw, "{0}</h:outputText>", indentacao);
		}
		else {
			println(pw, "{0}<h:outputText value=\"#'{'{1}'}'\" />", indentacao, value);
		}
	}
	
	protected void printot(PrintWriter pw, String indentation, String path, Attribute attribute) {

		Field field = getField(attribute);
		String value = path + getValue(attribute);

		if (BaseEnum.class.isAssignableFrom(field.getType())) {
			println(pw, "{0}<h:outputText value=\"#'{'{1}.descricao'}'\" />", indentation, value);
		}
		else if (Date.class.isAssignableFrom(field.getType())) {
			
			println(pw, "{0}<h:outputText value=\"#'{'{1}'}'\">", indentation, value);
			
			if (field.getAnnotation(Temporal.class).value() == TemporalType.DATE) {
				println(pw, "{0}\t<f:convertDateTime locale=\"pt_BR\" type=\"date\" />", indentation);
			}
			else if (field.getAnnotation(Temporal.class).value() == TemporalType.TIME) {
				println(pw, "{0}\t<f:convertDateTime locale=\"pt_BR\" type=\"time\" />", indentation);
			}
			else {
				println(pw, "{0}\t<f:convertDateTime locale=\"pt_BR\" type=\"both\" />", indentation);
			}
			
			println(pw, "{0}</h:outputText>", indentation);
		}
		else {
			println(pw, "{0}<h:outputText value=\"#'{'{1}'}'\" />", indentation, value);
		}
	}
}