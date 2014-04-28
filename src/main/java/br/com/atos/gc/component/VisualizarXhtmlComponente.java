package br.com.atos.gc.component;

import java.io.PrintWriter;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.model.AttributeManyToOne;
import br.com.atos.gc.model.AttributeOneToMany;
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
		
		for (Attribute atributo : getGc().getEntity().getAttributes()) {
			
			// Ignora as associacoes OneToMany
			if (!AttributeOneToMany.class.isInstance(atributo)) {
		
				println(pw, "\t\t\t\t\t\t\t\t");
				println(pw, "\t\t\t\t\t\t\t\t\t<h:outputText value=\"{0}\" />", atributo.getLabel());
				
				if (AttributeManyToOne.class.isInstance(atributo)) {
					AttributeManyToOne atributoManyToOne =  (AttributeManyToOne) atributo;
					printOutputText(pw, "\t\t\t\t\t\t\t\t\t", atributoManyToOne.getDescriptionAttributeOfAssociationField(), path + atributo.getField().getName() + "." + atributoManyToOne.getDescriptionAttributeOfAssociation());
				}
				else {
					printOutputText(pw, "\t\t\t\t\t\t\t\t\t", atributo.getField(), path + atributo.getField().getName());
				}
			}
		}

		println(pw, "\t\t\t\t\t\t\t\t</h:panelGrid>");		
		println(pw, "\t\t\t\t\t\t\t</p:tab>");
		
		for (AttributeOneToMany atributo : getGc().getEntity().getAttributesOneToMany()) {
			
			println(pw, "\t\t\t\t\t\t\t<p:tab title=\"{0}\">", atributo.getLabel());
			
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
			println(pw, "\t\t\t\t\t\t\t\t\t\t<h:link outcome=\"/pages/{0}/{0}Visualizar.jsf\" title=\"Visualizar o objeto\">", StringUtils.firstToLowerCase(atributo.getAssociationClassSimpleName()));
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
