package br.com.atos.cg.component;

import static br.com.atos.utils.StringUtils.firstToLowerCase;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.cg.model.Attribute;
import br.com.atos.cg.model.AttributeManyToOne;
import br.com.atos.core.model.BaseEnum;
import br.com.atos.core.model.IBaseEntity;
import br.com.atos.core.util.JpaReflectionUtils;

abstract public class Component {

	private CodeGenerator gc;
	
	public Component(CodeGenerator gc) {
		this.gc = gc;
	}

	public CodeGenerator getGc() {
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
		
	protected void printot(PrintWriter pw, String indentation, String path, Attribute attribute) {

		Field field = getField(attribute);
		String value = path + "." + getValue(attribute);

		if (BaseEnum.class.isAssignableFrom(field.getType())) {
			println(pw, "{0}<h:outputText value=\"#'{'{1}.descricao'}'\" />", indentation, value);
		}
		else if (Date.class.isAssignableFrom(field.getType())) {
			
			println(pw, "{0}<h:outputText value=\"#'{'{1}'}'\">", indentation, value);
			
			Temporal annotation = field.getAnnotation(Temporal.class);
			
			if (annotation == null || annotation.value() == TemporalType.TIMESTAMP) {
				println(pw, "{0}\t<f:convertDateTime locale=\"pt_BR\" type=\"both\" />", indentation);
			}
			else if (annotation.value() == TemporalType.DATE) {
				println(pw, "{0}\t<f:convertDateTime locale=\"pt_BR\" type=\"date\" />", indentation);
			}
			else if (annotation.value() == TemporalType.TIME) {
				println(pw, "{0}\t<f:convertDateTime locale=\"pt_BR\" type=\"time\" />", indentation);
			}			
			
			println(pw, "{0}</h:outputText>", indentation);
		}
		else {
			println(pw, "{0}<h:outputText value=\"#'{'{1}'}'\" />", indentation, value);
		}
	}
	
	protected void printin(PrintWriter pw, String indentation, String path, Attribute attribute) {
		printin(pw, indentation, path, attribute, false);
	}
	
	protected void printin(PrintWriter pw, String indentation, String path, Attribute attribute, boolean entityTab) {
			
		String id = attribute.getField().getName();
		String label = attribute.getLabel();
		Class<?> type = attribute.getField().getType();
		String value = path + "." + attribute.getField().getName();
		
		String required = "false";
	
		if (attribute.getField().getAnnotation(Column.class) != null) {
			required = attribute.getField().getAnnotation(Column.class).nullable() ? "false" : "true";
		}
		else if (attribute.getField().getAnnotation(OneToOne.class) != null) {
			required = attribute.getField().getAnnotation(OneToOne.class).optional() ? "false" : "true";
		}
		else if (attribute.getField().getAnnotation(ManyToOne.class) != null) {
			required = attribute.getField().getAnnotation(ManyToOne.class).optional() ? "false" : "true";
		}
		else if (attribute.getField().getAnnotation(JoinColumn.class) != null) {
			required = attribute.getField().getAnnotation(JoinColumn.class).nullable() ? "false" : "true";
		}
		
		if (BaseEnum.class.isAssignableFrom(type)) {
			println(pw, indentation + "<p:selectOneMenu id=\"{0}\" label=\"{1}\" value=\"#'{'{2}'}'\" effectDuration=\"0\" required=\"{3}\">", id, label, value, required);
			println(pw, indentation + "\t<f:selectItems value=\"#'{'selectItems.{0}Itens'}'\" />", firstToLowerCase(type.getSimpleName()));
			println(pw, indentation + "</p:selectOneMenu>");
		}
		else if (Date.class.isAssignableFrom(type) || Calendar.class.isAssignableFrom(type)) {

			Temporal annotation = attribute.getField().getAnnotation(Temporal.class);
			
			if (annotation == null || annotation.value() == TemporalType.TIMESTAMP) {
				println(pw, indentation + "<p:calendar id=\"{0}\" label=\"{1}\" value=\"#'{'{2}'}'\" showOn=\"button\" locale=\"pt_BR\" pattern=\"dd/MM/yyyy HH:mm\" required=\"{3}\" onkeypress=\"Mask.valid(this, ''dataHorario'')\" />", id, label, value, required);
			}
			else if (annotation.value() == TemporalType.DATE) {
				println(pw, indentation + "<p:calendar id=\"{0}\" label=\"{1}\" value=\"#'{'{2}'}'\" showOn=\"button\" locale=\"pt_BR\" pattern=\"dd/MM/yyyy\" required=\"{3}\" onkeypress=\"Mask.valid(this, ''data'')\" />", id, label, value, required);
			}
			else if (attribute.getField().getAnnotation(Temporal.class).value() == TemporalType.TIME) {
				println(pw, indentation + "<p:calendar id=\"{0}\" label=\"{1}\" value=\"#'{'{2}'}'\" size=\"4\" showOn=\"button\" locale=\"pt_BR\" pattern=\"HH:mm\" timeOnly=\"true\" required=\"{3}\" onkeypress=\"Mask.valid(this, ''horario'')\" />", id, label, value, required);
			}
		}
		else if (IBaseEntity.class.isAssignableFrom(type)) {
			
			AttributeManyToOne atributoManyToOne = (AttributeManyToOne) attribute;

			// Imprime um selectOneMenu
			//println(pw, indentation + "<p:selectOneMenu id=\"{0}\" label=\"{1}\" value=\"#'{'{2}'}'\" effectDuration=\"0\" required=\"{3}\" converter=\"simpleEntityConverter\">", atributo.getField().getName(), atributoLabel);
			//println(pw, indentation + "\t<f:selectItems value=\"#'{'selectItems.{0}Itens'}'\" />", firstToLowerCase(atributo.getField().getType().getSimpleName()));
			//println(pw, indentation + "</p:selectOneMenu>");
			
			Field associacaoFieldId = JpaReflectionUtils.getFieldId(type);

			// Imprime um autocomplete
			println(pw, indentation + "<p:autoComplete id=\"{0}\" label=\"{1}\" value=\"#'{'{2}'}'\" required=\"{3}\" forceSelection=\"true\"", id, label, value, required);

			if (entityTab) {
				println(pw, indentation + "\tdisabled=\"#'{'cc.attrs.winFrm.entidadeAssociada != null and cc.attrs.winFrm.entidadeAssociada == cc.attrs.winFrm.objeto.{0}'}'\"", attribute.getField().getName());
			}
			
			println(pw, indentation + "\tcompleteMethod=\"#'{'autoCompleteCtrl.onComplete{0}'}'\" dropdown=\"true\" converter=\"lazyEntityConverter\"", type.getSimpleName());
			println(pw, indentation + "\tvar=\"{0}\" itemValue=\"#'{'{0}'}'\" itemLabel=\"#'{'{0}.{1}'}'\"", firstToLowerCase(type.getSimpleName()), atributoManyToOne.getDescriptionAttributeOfAssociation());
			println(pw, indentation + "\tsize=\"{0}\" scrollHeight=\"200\">", entityTab ? "40" : "35");
			println(pw, indentation + "\t<p:column><h:outputText value=\"#'{'{0}.{1}'}'\" /></p:column>", firstToLowerCase(type.getSimpleName()), associacaoFieldId.getName());
			println(pw, indentation + "\t<p:column><h:outputText value=\"#'{'{0}.{1}'}'\" /></p:column>", firstToLowerCase(type.getSimpleName()), atributoManyToOne.getDescriptionAttributeOfAssociation());
			println(pw, indentation + "</p:autoComplete>");
		}
		else {
			println(pw, indentation + "<p:inputText id=\"{0}\" label=\"{1}\" value=\"#'{'{2}'}'\" required=\"{3}\" style=\"width: {4}px\" />", id, label, value, required, entityTab ? "300" : "100");
		}
	}
}