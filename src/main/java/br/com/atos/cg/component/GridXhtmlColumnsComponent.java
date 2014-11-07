package br.com.atos.cg.component;

import java.io.PrintWriter;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.cg.model.Attribute;
import br.com.atos.cg.model.AttributeOneToMany;

public class GridXhtmlColumnsComponent extends Component {

	public GridXhtmlColumnsComponent(CodeGenerator gc) {
		super(gc);
	}

	@Override
	public String getComponentKey() {
		return "gridXhtmlColumns";
	}

	@Override
	public void render(PrintWriter pw) {
		
		String path = "entity";
		
		for (Attribute attribute : getGc().getEntity().getAttributes()) {
			
			// Verificar se e para renderizar o filtro
			if (attribute.isRenderColumn()) {
			
				// Ignora as associacoes OneToMany
				if (!AttributeOneToMany.class.isInstance(attribute)) {
					println(pw, "\t\t\t");
					println(pw, "\t\t\t<p:column sortBy=\"#'{'{0}.{1}'}'\">", path, getValue(attribute));
					println(pw, "\t\t\t\t<f:facet name=\"header\"><h:outputText value=\"{0}\" /></f:facet>", attribute.getLabel());
					printot(pw, "\t\t\t\t", path, attribute);
					println(pw, "\t\t\t</p:column>");
				}
			}
		}
	}
}