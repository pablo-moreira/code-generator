package br.com.atos.gc.component;

import java.io.PrintWriter;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.model.AttributeManyToOne;
import br.com.atos.gc.model.AttributeOneToMany;

public class GridXhtmlColunasComponente extends Componente {

	public GridXhtmlColunasComponente(GeradorCodigo gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "gridXhtmlColunas";
	}

	@Override
	public void renderizar(PrintWriter pw) {
		
		String path = "objeto.";
		
		for (Attribute atributo : getGc().getEntity().getAttributes()) {
			
			// Ignora as associacoes OneToMany
			if (!AttributeOneToMany.class.isInstance(atributo)) {
				
				if (AttributeManyToOne.class.isInstance(atributo)) {
					AttributeManyToOne atributoManyToOne =  (AttributeManyToOne) atributo;
					println(pw, "\t\t\t<p:column sortBy=\"#'{'objeto.{1}.{2}'}'\">", atributo.getLabel(), atributo.getField().getName(), atributoManyToOne.getDescriptionAttributeOfAssociation());
					println(pw, "\t\t\t\t<f:facet name=\"header\"><h:outputText value=\"{0}\" /></f:facet>", atributo.getLabel());
					printOutputText(pw, "\t\t\t\t", atributoManyToOne.getDescriptionAttributeOfAssociationField(), path + atributo.getField().getName() + "." + atributoManyToOne.getDescriptionAttributeOfAssociation());
					println(pw, "\t\t\t</p:column>");
				}
				else {
					println(pw, "\t\t\t<p:column sortBy=\"#'{'objeto.{1}'}'\">", atributo.getLabel(), atributo.getField().getName());
					println(pw, "\t\t\t\t<f:facet name=\"header\"><h:outputText value=\"{0}\" /></f:facet>", atributo.getLabel());
					printOutputText(pw, "\t\t\t\t", atributo.getField(), path + atributo.getField().getName());
					println(pw, "\t\t\t</p:column>");
				}
			}
			
			println(pw, "\t\t\t");
		}
	}
}