package br.com.atos.gc.component;

import java.io.PrintWriter;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.model.AttributeOneToMany;

public class GridXhtmlColumnsComponent extends Component {

	public GridXhtmlColumnsComponent(GeradorCodigo gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "gridXhtmlColunas";
	}

	@Override
	public void renderizar(PrintWriter pw) {
		
		String path = "objeto";
		
		for (Attribute attribute : getGc().getEntity().getAttributes()) {
			
			// Verificar se e para renderizar o filtro
			if (attribute.isRenderColumn()) {
			
				// Ignora as associacoes OneToMany
				if (!AttributeOneToMany.class.isInstance(attribute)) {
					println(pw, "\t\t\t");
					println(pw, "\t\t\t<p:column sortBy=\"#'{'{0}'}'\">", path + getValue(attribute));
					println(pw, "\t\t\t\t<f:facet name=\"header\"><h:outputText value=\"{0}\" /></f:facet>", attribute.getLabel());
					printot(pw, "\t\t\t\t", path, attribute);
					println(pw, "\t\t\t</p:column>");
				}
			}
		}
	}
}