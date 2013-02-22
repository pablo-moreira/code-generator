package br.com.atos.gc.componente;

import java.io.PrintWriter;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.modelo.Atributo;
import br.com.atos.gc.modelo.AtributoManyToOne;
import br.com.atos.gc.modelo.AtributoOneToMany;

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
		
		for (Atributo atributo : getGc().getEntidade().getAtributos()) {
			
			// Ignora as associacoes OneToMany
			if (!AtributoOneToMany.class.isInstance(atributo)) {
				
				if (AtributoManyToOne.class.isInstance(atributo)) {
					AtributoManyToOne atributoManyToOne =  (AtributoManyToOne) atributo;
					println(pw, "\t\t\t<p:column headerText=\"{0}\" sortBy=\"#'{'objeto.{1}.{2}'}'\">", atributo.getRotulo(), atributo.getField().getName(), atributoManyToOne.getAssociacaoAtributoDescricao());
					printOutputText(pw, "\t\t\t\t", atributoManyToOne.getAssociacaoAtributoField(), path + atributo.getField().getName() + "." + atributoManyToOne.getAssociacaoAtributoDescricao());
					println(pw, "\t\t\t</p:column>");
				}
				else {
					println(pw, "\t\t\t<p:column headerText=\"{0}\" sortBy=\"#'{'objeto.{1}'}'\">", atributo.getRotulo(), atributo.getField().getName());
					printOutputText(pw, "\t\t\t\t", atributo.getField(), path + atributo.getField().getName());
					println(pw, "\t\t\t</p:column>");
				}
			}
			
			println(pw, "\t\t\t");
		}
	}
}