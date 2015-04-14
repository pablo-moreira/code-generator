package br.com.atos.cg.component;

import static br.com.atos.utils.StringUtils.firstToLowerCase;

import java.io.PrintWriter;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.cg.model.Attribute;
import br.com.atos.cg.model.AttributeManyToOne;
import br.com.atos.cg.model.AttributeOneToMany;
import br.com.atos.core.model.BaseEnum;
import br.com.atos.faces.component.model.FilterDate;

public class GridXhtmlFilterComponent extends Component {

	public GridXhtmlFilterComponent(CodeGenerator gc) {
		super(gc);
	}

	@Override
	public String getComponentKey() {
		return "gridXhtmlFilters";
	}

	@Override
	public void render(PrintWriter pw) {

		for (Attribute attribute : getGc().getEntity().getAttributes()) {
			
			// Verificar se e para renderizar o filtro
			if (attribute.isRenderFilter()) {
				
				// Ignora as associacoes OneToMany
				if (!AttributeOneToMany.class.isInstance(attribute)) {
					
					if (AttributeManyToOne.class.isInstance(attribute)) {
						AttributeManyToOne atributoManyToOne =  (AttributeManyToOne) attribute;
						printFilter(pw, attribute, atributoManyToOne.getDescriptionAttributeOfAssociationType(), atributoManyToOne.getAnnotationOfDescriptionAttributeOfAssociation(Temporal.class), attribute.getName() + "." + atributoManyToOne.getDescriptionAttributeOfAssociation());
					}
					else {												
						printFilter(pw, attribute, attribute.getType(), attribute.getAnnotation(Temporal.class), attribute.getName());
					}
				}
			}
		}
	}
	
	private void printFilter(PrintWriter pw, Attribute attribute, Class<?> type, Temporal annotation, String attributePath) {
		
		if (Number.class.isAssignableFrom(type)) {				
			println(pw, "\t\t\t\t<atos:filterNumeric operatorDefault=\"=\" attribute=\"{0}\" label=\"{1}\" type=\"{2}\" />", attributePath, attribute.getLabel(), type.getSimpleName());	
		}
		else if (BaseEnum.class.isAssignableFrom(type)) {
			println(pw, "\t\t\t\t<atos:filterEnum operatorDefault=\"=\" attribute=\"{0}\" label=\"{1}\" options=\"#'{'selectItems.{2}Itens'}'\" />", attributePath, attribute.getLabel(), firstToLowerCase(type.getSimpleName()));
		}
		else if (Date.class.isAssignableFrom(type)) {
				
			if (annotation == null || annotation.value() == TemporalType.TIMESTAMP) {
				println(pw, "\t\t\t\t<atos:filterDate operatorDefault=\"contains\" attribute=\"{0}\" label=\"{1}\" datePattern=\"{2}\" />", attributePath, attribute.getLabel(), FilterDate.DATE_PATTERN_DATA_HORARIO);
			}
			else if (annotation.value() == TemporalType.DATE) {
				println(pw, "\t\t\t\t<atos:filterDate operatorDefault=\"contains\" attribute=\"{0}\" label=\"{1}\" datePattern=\"{2}\" />", attributePath, attribute.getLabel(), FilterDate.DATE_PATTERN_DATA);
			}
			else {
				// Nao faz nada...
			}
		}
		else {
			println(pw, "\t\t\t\t<atos:filterString operatorDefault=\"contains\" attribute=\"{0}\" label=\"{1}\" />", attributePath, attribute.getLabel());
		}
	}
}