package br.com.atos.gc.component;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.model.AttributeManyToOne;
import br.com.atos.gc.model.AttributeOneToMany;
import br.com.atosdamidia.componente.model.FilterDate;
import br.com.atosdamidia.comuns.modelo.BaseEnum;

public class GridXhtmlFilterComponent extends Component {

	public GridXhtmlFilterComponent(GeradorCodigo gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "gridXhtmlFiltros";
	}

	@Override
	public void renderizar(PrintWriter pw) {

		for (Attribute atributo : getGc().getEntity().getAttributes()) {
			
			// Ignora as associacoes OneToMany e atributo com campo enum
			if (!AttributeOneToMany.class.isInstance(atributo)) {
				
				if (AttributeManyToOne.class.isInstance(atributo)) {
					AttributeManyToOne atributoManyToOne =  (AttributeManyToOne) atributo;
					imprimirFiltro(pw, atributo, atributoManyToOne.getDescriptionAttributeOfAssociationField(), atributo.getField().getName() + "." + atributoManyToOne.getDescriptionAttributeOfAssociation());
				}
				else {				
					imprimirFiltro(pw, atributo, atributo.getField(), atributo.getField().getName());
				}
			}
		}
	}
	
	private void imprimirFiltro(PrintWriter pw, Attribute atributo, Field field, String atributoPath) {

		if (!BaseEnum.class.isAssignableFrom(field.getType())) {		
		
			if (Long.class.isAssignableFrom(field.getType()) || Integer.class.isAssignableFrom(field.getType())) {				
				println(pw, "\t\t\t\t<atos:filterNumeric operatorDefault=\"=\" attribute=\"{0}\" label=\"{1}\" type=\"{2}\" />", atributoPath, atributo.getLabel(), field.getType().getSimpleName());	
			}
			else if (Date.class.isAssignableFrom(field.getType())) {
		
				if (field.getAnnotation(Temporal.class).value() == TemporalType.DATE) {
					println(pw, "\t\t\t\t<atos:filterDate operatorDefault=\"contains\" attribute=\"{0}\" label=\"{1}\" datePattern=\"{2}\" />", atributoPath, atributo.getLabel(), FilterDate.DATE_PATTERN_DATA);
				}
				else if (field.getAnnotation(Temporal.class).value() == TemporalType.TIMESTAMP) {
					println(pw, "\t\t\t\t<atos:filterDate operatorDefault=\"contains\" attribute=\"{0}\" label=\"{1}\" datePattern=\"{2}\" />", atributoPath, atributo.getLabel(), FilterDate.DATE_PATTERN_DATA_HORARIO);
				}
				else {
					// Nao faz nada...
				}
			}
			else {
				println(pw, "\t\t\t\t<atos:filterString operatorDefault=\"contains\" attribute=\"{0}\" label=\"{1}\" />", atributoPath, atributo.getLabel());
			}
		}
	}
}
