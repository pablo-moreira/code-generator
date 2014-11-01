package br.com.atos.cg.component;

import java.io.PrintWriter;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.cg.model.Attribute;
import br.com.atos.cg.model.AttributeOneToMany;
import br.com.atos.utils.StringUtils;

public class ViewXhtmlComponent extends Component {

	public ViewXhtmlComponent(CodeGenerator gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "viewXhtml";
	}
	
	@Override
	public void renderizar(PrintWriter pw) {

		String ctrl = getGc().getAttributeValue("EntityName") + "VisualizarCtrl.";
		String path = ctrl + "entity";
		
		println(pw, "\t\t\t\t\t\t\t<p:tab title=\"{0}\">", getGc().getAttributeValue("EntidadeRotulo"));		
		println(pw, "\t\t\t\t\t\t\t\t<h:panelGrid columns=\"2\" cellpadding=\"5\">");
		
		for (Attribute attribute : getGc().getEntity().getAttributes()) {
			
			// Ignora as associacoes OneToMany
			if (!AttributeOneToMany.class.isInstance(attribute)) {
		
				println(pw, "\t\t\t\t\t\t\t\t");
				println(pw, "\t\t\t\t\t\t\t\t\t<h:outputText value=\"{0}\" />", attribute.getLabel());
				printot(pw, "\t\t\t\t\t\t\t\t\t", path, attribute);
			}
		}

		println(pw, "\t\t\t\t\t\t\t\t</h:panelGrid>");		
		println(pw, "\t\t\t\t\t\t\t</p:tab>");
		
		for (AttributeOneToMany attribute : getGc().getEntity().getAttributesOneToMany()) {
			
			println(pw, "\t\t\t\t\t\t\t<p:tab title=\"{0}\">", attribute.getLabel());
			
			println(pw, "\t\t\t\t\t\t\t\t<p:dataTable");
			println(pw, "\t\t\t\t\t\t\t\t\temptyMessage=\"Nenhum objeto cadastrado.\"");
			println(pw, "\t\t\t\t\t\t\t\t\tvalue=\"#'{'{0}{1}'}'\"", path + ".", attribute.getField().getName());
			println(pw, "\t\t\t\t\t\t\t\t\tvar=\"associacao\">");
			println(pw, "\t\t\t\t\t\t\t\t\t");			

			String assocPath = "associacao";
			
			for (Attribute assocAttribute : attribute.getAssociationAttributesWithoutAttributeMappedByAndAttributesOneToMany()) {
				
				if (assocAttribute.isRenderColumn()) {				
					println(pw, "\t\t\t\t\t\t\t\t\t<p:column headerText=\"{0}\">", assocAttribute.getLabel());				
					printot(pw, "\t\t\t\t\t\t\t\t\t\t", assocPath, assocAttribute);
					println(pw, "\t\t\t\t\t\t\t\t\t</p:column>");
					println(pw, "\t\t\t\t\t\t\t\t\t");
				}
			}
		
			println(pw, "\t\t\t\t\t\t\t\t\t<p:column headerText=\"Ação\" styleClass=\"col-acao\">");
			println(pw, "\t\t\t\t\t\t\t\t\t\t<h:link outcome=\"/pages/{0}/{0}Visualizar.jsf\" title=\"Visualizar o objeto\">", StringUtils.firstToLowerCase(attribute.getAssociationClassSimpleName()));
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