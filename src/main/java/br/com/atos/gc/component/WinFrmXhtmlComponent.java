package br.com.atos.gc.component;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Version;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.model.AttributeFormType;
import br.com.atos.gc.model.AttributeId;
import br.com.atos.gc.model.AttributeOneToMany;
import br.com.atos.utils.StringUtils;

public class WinFrmXhtmlComponent extends Component {

	public WinFrmXhtmlComponent(GeradorCodigo gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "winFrmXhtml";
	}

	@Override
	public void renderizar(PrintWriter pw) {
		
		println(pw, "\t\t\t<p:tabView id=\"tbView\" activeIndex=\"#{cc.attrs.winFrm.tabViewIndex}\">");	
		
		gerarTabEntidade(pw);
		
		for (AttributeOneToMany attribute : getGc().getEntity().getAttributesOneToMany()) {
			
			if (attribute.isRenderForm()) {						
			
				String dataTableId = "dt" + StringUtils.firstToUpperCase(attribute.getField().getName());
				
				println(pw, "\t\t\t\t<p:tab title=\"{0}\">", attribute.getLabel());
				
				println(pw, "\t\t\t\t\t<p:dataTable");
				println(pw, "\t\t\t\t\t\tid=\"{0}\"", dataTableId);
				println(pw, "\t\t\t\t\t\temptyMessage=\"Nenhum objeto cadastrado.\"");
				println(pw, "\t\t\t\t\t\tvalue=\"#'{'cc.attrs.winFrm.objeto.{0}'}'\"", attribute.getField().getName());
				println(pw, "\t\t\t\t\t\tvar=\"associacao\">");
				println(pw, "\t\t\t\t\t\t");
				
				if (AttributeFormType.EXTERNAL.equals(attribute.getFormType())) {
										
					String assocPath = "associacao.";
					
					for (Attribute assocAttribute : attribute.getAssociationAttributesWithoutAttributeMappedByAndAttributesOneToMany()) {
						
						if (assocAttribute.isRenderColumn()) {
							
							println(pw, "\t\t\t\t\t\t<p:column headerText=\"{0}\">", assocAttribute.getLabel());				
							printot(pw, "\t\t\t\t\t\t\t", assocPath, assocAttribute);
							println(pw, "\t\t\t\t\t\t</p:column>");
							println(pw, "\t\t\t\t\t\t");
						}
					}

					println(pw, "\t\t\t\t\t\t<p:column headerText=\"Ação\" styleClass=\"col-acao\">");
					println(pw, "\t\t\t\t\t\t\t<p:commandLink action=\"#'{'cc.attrs.winFrm.associacao{0}.iniciarEdicao'}'\" title=\"Alterar o objeto\" process=\"@this\">", StringUtils.firstToUpperCase(attribute.getField().getName()));
					println(pw, "\t\t\t\t\t\t\t\t<h:graphicImage value=\"/resources/img/s.gif\" styleClass=\"link-icone ui-icon-pencil\" />");
					println(pw, "\t\t\t\t\t\t\t\t<f:setPropertyActionListener target=\"#'{'cc.attrs.winFrm.associacao{0}.winFrmAssociacao.objeto}\" value=\"#'{'associacao'}'\" />", StringUtils.firstToUpperCase(attribute.getField().getName()));
					println(pw, "\t\t\t\t\t\t\t</p:commandLink>");
					println(pw, "\t\t\t\t\t\t\t");
					println(pw, "\t\t\t\t\t\t\t<p:commandLink action=\"#'{'cc.attrs.winFrm.associacao{0}.iniciarExclusao'}'\" title=\"Excluir o objeto\" process=\"@this\">", StringUtils.firstToUpperCase(attribute.getField().getName()));
					println(pw, "\t\t\t\t\t\t\t\t<h:graphicImage value=\"/resources/img/s.gif\" styleClass=\"link-icone ui-icon-trash\" />");
					println(pw, "\t\t\t\t\t\t\t\t<f:setPropertyActionListener target=\"#'{'cc.attrs.winFrm.associacao{0}.winFrmAssociacao.objeto}\" value=\"#'{'associacao'}'\" />", StringUtils.firstToUpperCase(attribute.getField().getName()));
					println(pw, "\t\t\t\t\t\t\t</p:commandLink>");
					println(pw, "\t\t\t\t\t\t</p:column>");					
					println(pw, "\t\t\t\t\t\t");
					println(pw, "\t\t\t\t\t\t<f:facet name=\"footer\">");
					println(pw, "\t\t\t\t\t\t\t<p:outputPanel styleClass=\"datatable-menu\" layout=\"block\">");
					println(pw, "\t\t\t\t\t\t\t\t<p:commandButton value=\"Adicionar\" action=\"#'{'cc.attrs.winFrm.associacao{0}.iniciarInsercao'}'\" title=\"Adiconar um ojbeto\" icon=\"ui-icon-document\" process=\"@this\" />", StringUtils.firstToUpperCase(attribute.getField().getName()));
					println(pw, "\t\t\t\t\t\t\t</p:outputPanel>");
					println(pw, "\t\t\t\t\t\t</f:facet>");
					println(pw, "\t\t\t\t\t</p:dataTable>");	
				}
				else {
					
					String assocPath = "associacao";
					
					AttributeId assocAttributeId = attribute.getAssociationEntity().getAttributeId();
					
					println(pw, "\t\t\t\t\t\t<p:column headerText=\"{0}\" sortBy=\"#'{'{1}.{2}'}'\" filterBy=\"#'{'{1}.{2}'}'\">", assocAttributeId.getLabel(), assocPath, getValue(assocAttributeId));
					println(pw, "\t\t\t\t\t\t\t<h:outputText value=\"#'{'{0}.{1}'}'\" />", assocPath, assocAttributeId.getField().getName());
					println(pw, "\t\t\t\t\t\t</p:column>");
					println(pw, "\t\t\t\t\t\t");
					
					for (Attribute assocAttribute : attribute.getAssociationAttributesWithoutAttributeMappedByAndAttributesOneToMany()) {
						
						if (assocAttribute.isRenderColumn() && !(assocAttribute instanceof AttributeId)) {
							
							println(pw, "\t\t\t\t\t\t<p:column headerText=\"{0}\" sortBy=\"#'{'{1}.{2}'}'\" filterBy=\"#'{'{1}.{2}'}'\">", assocAttribute.getLabel(), assocPath, getValue(assocAttribute));
							println(pw, "\t\t\t\t\t\t\t<h:panelGrid columns=\"2\" styleClass=\"semborda\">");
							printin(pw, "\t\t\t\t\t\t\t\t", assocPath, assocAttribute, false);
							println(pw, "\t\t\t\t\t\t\t\t<p:message for=\"{0}\" display=\"icon\" />", assocAttribute.getField().getName());
							println(pw, "\t\t\t\t\t\t\t</h:panelGrid>");
							println(pw, "\t\t\t\t\t\t</p:column>");
							println(pw, "\t\t\t\t\t\t");
						}
					}
					
					println(pw, "\t\t\t\t\t\t<p:column headerText=\"Ação\" styleClass=\"col-acao\">");
					println(pw, "\t\t\t\t\t\t\t<p:commandLink action=\"#'{'cc.attrs.winFrm.associacao{0}.iniciarExclusao'}'\" title=\"Excluir o objeto\" process=\"@this\">", StringUtils.firstToUpperCase(attribute.getField().getName()));
					println(pw, "\t\t\t\t\t\t\t\t<h:graphicImage value=\"/resources/img/s.gif\" styleClass=\"link-icone ui-icon-trash\" />");
					println(pw, "\t\t\t\t\t\t\t\t<f:setPropertyActionListener target=\"#'{'cc.attrs.winFrm.associacao{0}.winFrmAssociacao.objeto}\" value=\"#'{'associacao'}'\" />", StringUtils.firstToUpperCase(attribute.getField().getName()));
					println(pw, "\t\t\t\t\t\t\t</p:commandLink>");
					println(pw, "\t\t\t\t\t\t</p:column>");
					println(pw, "\t\t\t\t\t\t");
					println(pw, "\t\t\t\t\t\t<f:facet name=\"footer\">");
					println(pw, "\t\t\t\t\t\t\t<p:outputPanel styleClass=\"datatable-menu\" layout=\"block\">");
					println(pw, "\t\t\t\t\t\t\t\t<p:commandButton value=\"Adicionar\" action=\"#'{'cc.attrs.winFrm.associacao{0}.iniciarInsercao'}'\" title=\"Adiconar um ojbeto\" icon=\"ui-icon-document\" process=\"{1}\" update=\"{1}\" />", StringUtils.firstToUpperCase(attribute.getField().getName()), dataTableId);
					println(pw, "\t\t\t\t\t\t\t</p:outputPanel>");
					println(pw, "\t\t\t\t\t\t</f:facet>");
					println(pw, "\t\t\t\t\t</p:dataTable>");
					println(pw, "\t\t\t\t\t");
					println(pw, "\t\t\t\t\t<atos:winFrmAssociacaoExcluir");
					println(pw, "\t\t\t\t\t\tfrmAssociacao=\"#'{'cc.attrs.winFrm.associacao{0}'}'\"", StringUtils.firstToUpperCase(attribute.getField().getName()));
					println(pw, "\t\t\t\t\t\ttitulo=\"Exclusão de {0}.\"", attribute.getLabel());
					println(pw, "\t\t\t\t\t\tmensagem=\"Você tem certeza que deseja excluir {0}?\" />", attribute.getLabel());
				}
		
				println(pw, "\t\t\t\t</p:tab>");
			}
		}

		print(pw, "\t\t\t</p:tabView>");
	}

	private void gerarTabEntidade(PrintWriter pw) {

		String path = "cc.attrs.winFrm.objeto";
		
		println(pw, "\t\t\t\t<p:tab title=\"{0}\">", getGc().getAttributeValue("EntidadeRotulo"));
		println(pw, "\t\t\t\t\t<h:panelGrid columns=\"3\" cellpadding=\"5\" style=\"width: 100%\">");
		println(pw, "\t\t\t\t\t\t<h:outputLabel value=\"Id:\" for=\"id\" />");
		println(pw, "\t\t\t\t\t\t<p:inputText id=\"id\" label=\"Id.\" value=\"#'{'{0}.id'}'\" disabled=\"true\" />", path);
		println(pw, "\t\t\t\t\t\t<p:message for=\"id\" display=\"icon\" />");
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		for (Attribute attribute : getGc().getEntity().getAttributes()) {
			
			if (attribute.isRenderForm() && !AttributeId.class.isInstance(attribute) && !AttributeOneToMany.class.isInstance(attribute)) {
					
				// Verifica se o atributo e anotado com @Version
				if (attribute.getField().getAnnotation(Version.class) != null) {
					println(pw, "\t\t\t\t\t\t");
					println(pw, "\t\t\t\t\t\t<h:outputLabel value=\"{0}:\" for=\"{1}\" />", attribute.getLabel(), attribute.getField().getName());
					println(pw, "\t\t\t\t\t\t<p:inputText id=\"{0}\" label=\"{1}\" value=\"#'{'{2}.{1}'}'\" disabled=\"true\" />", attribute.getField().getName(), attribute.getLabel(), path);
					println(pw, "\t\t\t\t\t\t<p:message for=\"{0}\" display=\"icon\" />", attribute.getField().getName());
				}
				else {
					attributes.add(attribute);
				}
			}
		}

		for (Attribute attribute : attributes) {
			println(pw, "\t\t\t\t\t\t");
			println(pw, "\t\t\t\t\t\t<h:outputLabel value=\"{0}:\" for=\"{1}\" />", attribute.getLabel(), attribute.getField().getName());			
			printin(pw, "\t\t\t\t\t\t", path, attribute, true);
			println(pw, "\t\t\t\t\t\t<p:message for=\"{0}\" display=\"icon\" />", attribute.getField().getName());
		}

		println(pw, "\t\t\t\t\t</h:panelGrid>");
		println(pw, "\t\t\t\t</p:tab>");		
	}
}
