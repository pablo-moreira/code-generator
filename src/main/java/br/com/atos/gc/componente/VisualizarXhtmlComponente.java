package br.com.atos.gc.componente;

import java.io.PrintWriter;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.modelo.Atributo;
import br.com.atos.gc.modelo.AtributoManyToOne;
import br.com.atos.gc.modelo.AtributoOneToMany;
import br.com.atos.utils.StringUtils;

public class VisualizarXhtmlComponente extends Componente {

	public VisualizarXhtmlComponente(GeradorCodigo gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "visualizarXhtml";
	}
	
	@Override
	public void renderizar(PrintWriter pw) {

		String ctrl = getGc().getAtributoValor("entidadeNome") + "VisualizarCtrl.";
		String path = ctrl + "entidade.";
		
		println(pw, "\t\t\t\t\t\t\t<p:tab title=\"{0}\">", getGc().getAtributoValor("EntidadeRotulo"));		
		println(pw, "\t\t\t\t\t\t\t\t<h:panelGrid columns=\"2\" cellpadding=\"5\">");
		
		for (Atributo atributo : getGc().getEntidade().getAtributos()) {
			
			// Ignora as associacoes OneToMany
			if (!AtributoOneToMany.class.isInstance(atributo)) {
		
				println(pw, "\t\t\t\t\t\t\t\t");
				println(pw, "\t\t\t\t\t\t\t\t\t<h:outputText value=\"{0}\" />", atributo.getRotulo());
				
				if (AtributoManyToOne.class.isInstance(atributo)) {
					AtributoManyToOne atributoManyToOne =  (AtributoManyToOne) atributo;
					printOutputText(pw, "\t\t\t\t\t\t\t\t\t", atributoManyToOne.getAssociacaoAtributoField(), path + atributo.getField().getName() + "." + atributoManyToOne.getAssociacaoAtributoDescricao());
				}
				else {
					printOutputText(pw, "\t\t\t\t\t\t\t\t\t", atributo.getField(), path + atributo.getField().getName());
				}
			}
		}

		println(pw, "\t\t\t\t\t\t\t\t</h:panelGrid>");		
		println(pw, "\t\t\t\t\t\t\t</p:tab>");
		
		for (AtributoOneToMany atributo : getGc().getEntidade().getAtributosOneToMany()) {
			
			println(pw, "\t\t\t\t\t\t\t<p:tab title=\"{0}\">", atributo.getRotulo());
			
			println(pw, "\t\t\t\t\t\t\t\t<p:dataTable");
			println(pw, "\t\t\t\t\t\t\t\t\temptyMessage=\"Nenhum objeto cadastrado.\"");
			println(pw, "\t\t\t\t\t\t\t\t\tvalue=\"#'{'{0}{1}'}'\"", path, atributo.getField().getName());
			println(pw, "\t\t\t\t\t\t\t\t\tvar=\"associacao\">");
			println(pw, "\t\t\t\t\t\t\t\t\t");			
			println(pw, "\t\t\t\t\t\t\t\t\t<p:column headerText=\"Id\">");
			println(pw, "\t\t\t\t\t\t\t\t\t\t<h:outputText value=\"#{associacao.id}\" />");
			println(pw, "\t\t\t\t\t\t\t\t\t</p:column>");			
			println(pw, "\t\t\t\t\t\t\t\t\t");
			println(pw, "\t\t\t\t\t\t\t\t\t<p:column headerText=\"Ação\" styleClass=\"col-acao\">");
			println(pw, "\t\t\t\t\t\t\t\t\t\t<h:link outcome=\"/pages/{0}/{0}Visualizar.jsf\" title=\"Visualizar o objeto\">", StringUtils.firstToLowerCase(atributo.getAssociacaoTipoSimpleName()));
			println(pw, "\t\t\t\t\t\t\t\t\t\t\t<f:param name=\"id\" value=\"#{associacao.id}\" />");
			println(pw, "\t\t\t\t\t\t\t\t\t\t\t<f:param name=\"revisaoId\" value=\"#'{'{0}'}'\" rendered=\"#'{'{1}'}'\" />", ctrl + "revisaoId", ctrl + "revisaoId != null");
			println(pw, "\t\t\t\t\t\t\t\t\t\t\t<h:graphicImage value=\"/resources/img/s.gif\" styleClass=\"link-icone ui-icon-zoomin\" />");
			println(pw, "\t\t\t\t\t\t\t\t\t\t</h:link>");
			println(pw, "\t\t\t\t\t\t\t\t\t</p:column>");			
			
			println(pw, "\t\t\t\t\t\t\t\t</p:dataTable>");
			println(pw, "\t\t\t\t\t\t\t</p:tab>");
		}
	}
}
