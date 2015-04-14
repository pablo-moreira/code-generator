package br.com.atos.cg.component;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Version;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.cg.model.Attribute;
import br.com.atos.cg.model.AttributeFormType;
import br.com.atos.cg.model.AttributeId;
import br.com.atos.cg.model.AttributeOneToMany;
import br.com.atos.utils.StringUtils;

public class WinFrmXhtmlComponent extends Component {

	public WinFrmXhtmlComponent(CodeGenerator gc) {
		super(gc);
	}

	@Override
	public String getComponentKey() {
		return "winFrmXhtml";
	}

	@Override
	public void render(PrintWriter pw) {
		
		println(pw, "\t\t\t<p:tabView id=\"tbView\" activeIndex=\"#{cc.attrs.winFrm.tabViewIndex}\">");	
		
		gerarTabEntidade(pw);
		
		for (AttributeOneToMany attribute : getGc().getEntity().getAttributesOneToMany()) {
			
			if (attribute.isRenderForm()) {						
			
				String dataTableId = "dt" + StringUtils.firstToUpperCase(attribute.getName());
				
				println(pw, "\t\t\t\t<p:tab title=\"{0}\">", attribute.getLabel());
				
				println(pw, "\t\t\t\t\t<p:dataTable");
				println(pw, "\t\t\t\t\t\tid=\"{0}\"", dataTableId);
				println(pw, "\t\t\t\t\t\temptyMessage=\"Nenhum objeto cadastrado.\"");
				println(pw, "\t\t\t\t\t\tvalue=\"#'{'cc.attrs.winFrm.entity.{0}'}'\"", attribute.getName());
				println(pw, "\t\t\t\t\t\tvar=\"association\">");
				println(pw, "\t\t\t\t\t\t");
				
				if (AttributeFormType.EXTERNAL.equals(attribute.getFormType())) {
										
					String assocPath = "associacao";
					
					for (Attribute assocAttribute : attribute.getAssociationAttributesWithoutAttributeMappedByAndAttributesOneToMany()) {
						
						if (assocAttribute.isRenderColumn()) {
							
							println(pw, "\t\t\t\t\t\t<p:column headerText=\"{0}\" sortBy=\"#'{'{1}.{2}'}'\" filterBy=\"#'{'{1}.{2}'}'\">", assocAttribute.getLabel(), assocPath, getValue(assocAttribute));		
							printot(pw, "\t\t\t\t\t\t\t", assocPath, assocAttribute);
							println(pw, "\t\t\t\t\t\t</p:column>");
							println(pw, "\t\t\t\t\t\t");
						}
					}

					println(pw, "\t\t\t\t\t\t<p:column headerText=\"Ação\" styleClass=\"col-acao\">");
					println(pw, "\t\t\t\t\t\t\t<p:commandLink action=\"#'{'cc.attrs.winFrm.association{0}.startUpdate'}'\" title=\"Alterar o objeto\" process=\"@this\">", StringUtils.firstToUpperCase(attribute.getName()));
					println(pw, "\t\t\t\t\t\t\t\t<h:graphicImage value=\"/resources/img/s.gif\" styleClass=\"link-icone ui-icon-pencil\" />");
					println(pw, "\t\t\t\t\t\t\t\t<f:setPropertyActionListener target=\"#'{'cc.attrs.winFrm.association{0}.winFrmAssociation.entity}\" value=\"#'{'association'}'\" />", StringUtils.firstToUpperCase(attribute.getName()));
					println(pw, "\t\t\t\t\t\t\t</p:commandLink>");
					println(pw, "\t\t\t\t\t\t\t");
					println(pw, "\t\t\t\t\t\t\t<p:commandLink action=\"#'{'cc.attrs.winFrm.association{0}.startDelete'}'\" title=\"Excluir o objeto\" process=\"@this\">", StringUtils.firstToUpperCase(attribute.getName()));
					println(pw, "\t\t\t\t\t\t\t\t<h:graphicImage value=\"/resources/img/s.gif\" styleClass=\"link-icone ui-icon-trash\" />");
					println(pw, "\t\t\t\t\t\t\t\t<f:setPropertyActionListener target=\"#'{'cc.attrs.winFrm.association{0}.winFrmAssociation.entity}\" value=\"#'{'association'}'\" />", StringUtils.firstToUpperCase(attribute.getName()));
					println(pw, "\t\t\t\t\t\t\t</p:commandLink>");
					println(pw, "\t\t\t\t\t\t</p:column>");					
					println(pw, "\t\t\t\t\t\t");
					println(pw, "\t\t\t\t\t\t<f:facet name=\"footer\">");
					println(pw, "\t\t\t\t\t\t\t<p:outputPanel styleClass=\"datatable-menu\" layout=\"block\">");
					println(pw, "\t\t\t\t\t\t\t\t<p:commandButton value=\"Adicionar\" action=\"#'{'cc.attrs.winFrm.association{0}.startInsert'}'\" title=\"Adiconar um ojbeto\" icon=\"ui-icon-document\" process=\"@this\" />", StringUtils.firstToUpperCase(attribute.getName()));
					println(pw, "\t\t\t\t\t\t\t</p:outputPanel>");
					println(pw, "\t\t\t\t\t\t</f:facet>");
					println(pw, "\t\t\t\t\t</p:dataTable>");	
				}
				else {
					
					String assocPath = "association";
					
					AttributeId assocAttributeId = attribute.getAssociationEntity().getAttributeId();
					
					println(pw, "\t\t\t\t\t\t<p:column headerText=\"{0}\" sortBy=\"#'{'{1}.{2}'}'\" filterBy=\"#'{'{1}.{2}'}'\" filterStyle=\"width: 50px\" styleClass=\"width: 50px\">", assocAttributeId.getLabel(), assocPath, getValue(assocAttributeId));
					println(pw, "\t\t\t\t\t\t\t<h:outputText value=\"#'{'{0}.{1}'}'\" />", assocPath, assocAttributeId.getName());
					println(pw, "\t\t\t\t\t\t</p:column>");
					println(pw, "\t\t\t\t\t\t");
					
					for (Attribute assocAttribute : attribute.getAssociationAttributesWithoutAttributeMappedByAndAttributesOneToMany()) {
						
						if (assocAttribute.isRenderColumn() && !(assocAttribute instanceof AttributeId)) {
							
							println(pw, "\t\t\t\t\t\t<p:column headerText=\"{0}\" sortBy=\"#'{'{1}.{2}'}'\" filterBy=\"#'{'{1}.{2}'}'\">", assocAttribute.getLabel(), assocPath, getValue(assocAttribute));
							println(pw, "\t\t\t\t\t\t\t<h:panelGrid columns=\"2\" styleClass=\"semborda\">");
							printin(pw, "\t\t\t\t\t\t\t\t", assocPath, assocAttribute, false);
							println(pw, "\t\t\t\t\t\t\t\t<p:message for=\"{0}\" display=\"icon\" />", assocAttribute.getName());
							println(pw, "\t\t\t\t\t\t\t</h:panelGrid>");
							println(pw, "\t\t\t\t\t\t</p:column>");
							println(pw, "\t\t\t\t\t\t");
						}
					}
					
					println(pw, "\t\t\t\t\t\t<p:column headerText=\"Ação\" styleClass=\"col-acao\">");
					println(pw, "\t\t\t\t\t\t\t<p:commandLink action=\"#'{'cc.attrs.winFrm.association{0}.startUpdate'}'\" title=\"Excluir o objeto\" process=\"@this\">", StringUtils.firstToUpperCase(attribute.getName()));
					println(pw, "\t\t\t\t\t\t\t\t<h:graphicImage value=\"/resources/img/s.gif\" styleClass=\"link-icone ui-icon-trash\" />");
					println(pw, "\t\t\t\t\t\t\t\t<f:setPropertyActionListener target=\"#'{'cc.attrs.winFrm.association{0}.association}\" value=\"#'{'association'}'\" />", StringUtils.firstToUpperCase(attribute.getName()));
					println(pw, "\t\t\t\t\t\t\t</p:commandLink>");
					println(pw, "\t\t\t\t\t\t</p:column>");
					println(pw, "\t\t\t\t\t\t");
					println(pw, "\t\t\t\t\t\t<f:facet name=\"footer\">");
					println(pw, "\t\t\t\t\t\t\t<p:outputPanel styleClass=\"datatable-menu\" layout=\"block\">");
					println(pw, "\t\t\t\t\t\t\t\t<p:commandButton value=\"Adicionar\" action=\"#'{'cc.attrs.winFrm.association{0}.startInsert'}'\" title=\"Adiconar um ojbeto\" icon=\"ui-icon-document\" process=\"{1}\" update=\"{1}\" />", StringUtils.firstToUpperCase(attribute.getName()), dataTableId);
					println(pw, "\t\t\t\t\t\t\t</p:outputPanel>");
					println(pw, "\t\t\t\t\t\t</f:facet>");
					println(pw, "\t\t\t\t\t</p:dataTable>");
					println(pw, "\t\t\t\t\t");
					println(pw, "\t\t\t\t\t<atos:winFrmAssociationDelete");
					println(pw, "\t\t\t\t\t\tfrmAssociation=\"#'{'cc.attrs.winFrm.association{0}'}'\"", StringUtils.firstToUpperCase(attribute.getName()));
					println(pw, "\t\t\t\t\t\ttitle=\"Exclusão de {0}.\"", attribute.getLabel());
					println(pw, "\t\t\t\t\t\tmessage=\"Você tem certeza que deseja excluir {0}?\" />", attribute.getLabel());
				}
		
				println(pw, "\t\t\t\t</p:tab>");
			}
		}

		print(pw, "\t\t\t</p:tabView>");
	}

	private void gerarTabEntidade(PrintWriter pw) {

		String path = "cc.attrs.winFrm.entity";
		
		println(pw, "\t\t\t\t<p:tab title=\"{0}\">", getGc().getAttributeValue("EntityLabel"));
		println(pw, "\t\t\t\t\t<h:panelGrid columns=\"3\" cellpadding=\"5\" style=\"width: 100%\">");
		println(pw, "\t\t\t\t\t\t<h:outputLabel value=\"Id:\" for=\"id\" />");
		println(pw, "\t\t\t\t\t\t<p:inputText id=\"id\" label=\"Id.\" value=\"#'{'{0}.id'}'\" disabled=\"true\" />", path);
		println(pw, "\t\t\t\t\t\t<p:message for=\"id\" display=\"icon\" />");
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		for (Attribute attribute : getGc().getEntity().getAttributes()) {
			
			if (attribute.isRenderForm() && !AttributeId.class.isInstance(attribute) && !AttributeOneToMany.class.isInstance(attribute)) {
					
				// Verifica se o atributo e anotado com @Version
				if (attribute.getAnnotation(Version.class) != null) {
					println(pw, "\t\t\t\t\t\t");
					println(pw, "\t\t\t\t\t\t<h:outputLabel value=\"{0}:\" for=\"{1}\" />", attribute.getLabel(), attribute.getName());
					println(pw, "\t\t\t\t\t\t<p:inputText id=\"{0}\" label=\"{1}\" value=\"#'{'{2}.{1}'}'\" disabled=\"true\" />", attribute.getName(), attribute.getLabel(), path);
					println(pw, "\t\t\t\t\t\t<p:message for=\"{0}\" display=\"icon\" />", attribute.getName());
				}
				else {
					attributes.add(attribute);
				}
			}
		}

		for (Attribute attribute : attributes) {
			println(pw, "\t\t\t\t\t\t");
			println(pw, "\t\t\t\t\t\t<h:outputLabel value=\"{0}:\" for=\"{1}\" />", attribute.getLabel(), attribute.getName());			
			printin(pw, "\t\t\t\t\t\t", path, attribute, true);
			println(pw, "\t\t\t\t\t\t<p:message for=\"{0}\" display=\"icon\" />", attribute.getName());
		}

		println(pw, "\t\t\t\t\t</h:panelGrid>");
		println(pw, "\t\t\t\t</p:tab>");		
	}
}
