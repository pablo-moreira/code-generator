package br.com.atos.gc.component;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.model.AttributeFormType;
import br.com.atos.gc.model.AttributeId;
import br.com.atos.gc.model.AttributeManyToOne;
import br.com.atos.gc.model.AttributeOneToMany;
import br.com.atos.utils.StringUtils;
import static br.com.atos.utils.StringUtils.firstToLowerCase;
import br.com.atosdamidia.comuns.modelo.BaseEnum;
import br.com.atosdamidia.comuns.modelo.IBaseEntity;
import br.com.atosdamidia.comuns.util.JpaReflectionUtils;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

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
		
		for (AttributeOneToMany atributo : getGc().getEntity().getAttributesOneToMany()) { 
			println(pw, "\t\t\t\t<p:tab title=\"{0}\">", atributo.getLabel());
			
			println(pw, "\t\t\t\t\t<p:dataTable");
			println(pw, "\t\t\t\t\t\tid=\"dt{0}\"", StringUtils.firstToUpperCase(atributo.getField().getName()));
			println(pw, "\t\t\t\t\t\temptyMessage=\"Nenhum objeto cadastrado.\"");
			println(pw, "\t\t\t\t\t\tvalue=\"#'{'cc.attrs.winFrm.objeto.{0}'}'\"", atributo.getField().getName());
			println(pw, "\t\t\t\t\t\tvar=\"associacao\">");
			println(pw, "\t\t\t\t\t\t");
			
			if (AttributeFormType.EXTERNAL.equals(atributo.getFormType())) {
						
				println(pw, "\t\t\t\t\t\t<p:column headerText=\"Id\" sortBy=\"#{associacao.id}\">");
				println(pw, "\t\t\t\t\t\t\t<h:outputText value=\"#{associacao.id}\" />");
				println(pw, "\t\t\t\t\t\t</p:column>");
				println(pw, "\t\t\t\t\t\t");
							
				println(pw, "\t\t\t\t\t\t<p:column headerText=\"Ação\" styleClass=\"col-acao\">");
				println(pw, "\t\t\t\t\t\t\t<p:commandLink action=\"#'{'cc.attrs.winFrm.associacao{0}.iniciarEdicao'}'\" title=\"Alterar o objeto\" process=\"@this\">", StringUtils.firstToUpperCase(atributo.getField().getName()));
				println(pw, "\t\t\t\t\t\t\t\t<h:graphicImage value=\"/resources/img/s.gif\" styleClass=\"link-icone ui-icon-pencil\" />");
				println(pw, "\t\t\t\t\t\t\t\t<f:setPropertyActionListener target=\"#'{'cc.attrs.winFrm.associacao{0}.winFrmAssociacao.objeto}\" value=\"#'{'associacao'}'\" />", StringUtils.firstToUpperCase(atributo.getField().getName()));
				println(pw, "\t\t\t\t\t\t\t</p:commandLink>");
				println(pw, "\t\t\t\t\t\t\t");
				println(pw, "\t\t\t\t\t\t\t<p:commandLink action=\"#'{'cc.attrs.winFrm.associacao{0}.iniciarExclusao'}'\" title=\"Excluir o objeto\" process=\"@this\">", StringUtils.firstToUpperCase(atributo.getField().getName()));
				println(pw, "\t\t\t\t\t\t\t\t<h:graphicImage value=\"/resources/img/s.gif\" styleClass=\"link-icone ui-icon-trash\" />");
				println(pw, "\t\t\t\t\t\t\t\t<f:setPropertyActionListener target=\"#'{'cc.attrs.winFrm.associacao{0}.winFrmAssociacao.objeto}\" value=\"#'{'associacao'}'\" />", StringUtils.firstToUpperCase(atributo.getField().getName()));
				println(pw, "\t\t\t\t\t\t\t</p:commandLink>");
				println(pw, "\t\t\t\t\t\t</p:column>");			
			}
			else {				
				println(pw, "\t\t\t\t\t\t<p:column headerText=\"Id\" sortBy=\"#{associacao.id}\">");
				println(pw, "\t\t\t\t\t\t\t<h:outputText value=\"#{associacao.id}\" />");
				println(pw, "\t\t\t\t\t\t</p:column>");
				println(pw, "\t\t\t\t\t\t");
				
				println(pw, "\t\t\t\t\t\t<p:column headerText=\"Ação\" styleClass=\"col-acao\">");
				println(pw, "\t\t\t\t\t\t\t<p:commandLink action=\"#'{'cc.attrs.winFrm.associacao{0}.iniciarExclusao'}'\" title=\"Excluir o objeto\" process=\"@this\">", StringUtils.firstToUpperCase(atributo.getField().getName()));
				println(pw, "\t\t\t\t\t\t\t\t<h:graphicImage value=\"/resources/img/s.gif\" styleClass=\"link-icone ui-icon-trash\" />");
				println(pw, "\t\t\t\t\t\t\t\t<f:setPropertyActionListener target=\"#'{'cc.attrs.winFrm.associacao{0}.winFrmAssociacao.objeto}\" value=\"#'{'associacao'}'\" />", StringUtils.firstToUpperCase(atributo.getField().getName()));
				println(pw, "\t\t\t\t\t\t\t</p:commandLink>");
				println(pw, "\t\t\t\t\t\t</p:column>");
			}			
			
			println(pw, "\t\t\t\t\t\t");
			println(pw, "\t\t\t\t\t\t<f:facet name=\"footer\">");
			println(pw, "\t\t\t\t\t\t\t<p:outputPanel styleClass=\"datatable-menu\" layout=\"block\">");
			println(pw, "\t\t\t\t\t\t\t\t<p:commandButton value=\"Adicionar\" action=\"#'{'cc.attrs.winFrm.associacao{0}.iniciarInsercao'}'\" title=\"Adiconar um ojbeto\" icon=\"ui-icon-document\" process=\"@this\" />", StringUtils.firstToUpperCase(atributo.getField().getName()));
			println(pw, "\t\t\t\t\t\t\t</p:outputPanel>");
			println(pw, "\t\t\t\t\t\t</f:facet>");
			println(pw, "\t\t\t\t\t</p:dataTable>");			
			println(pw, "\t\t\t\t</p:tab>");
		}

		print(pw, "\t\t\t</p:tabView>");
	}

	private void gerarTabEntidade(PrintWriter pw) {

		println(pw, "\t\t\t\t<p:tab title=\"{0}\">", getGc().getAtributoValor("EntidadeRotulo"));
	
		println(pw, "\t\t\t\t\t<h:panelGrid columns=\"3\" cellpadding=\"5\" style=\"width: 100%\">");
		
		println(pw, "\t\t\t\t\t\t<h:outputLabel value=\"Id:\" for=\"id\" />");
		println(pw, "\t\t\t\t\t\t<p:inputText id=\"id\" label=\"Id.\" value=\"#{cc.attrs.winFrm.objeto.id}\" disabled=\"true\" />");
		println(pw, "\t\t\t\t\t\t<p:message for=\"id\" display=\"icon\" />");
		
		List<Attribute> atributos = new ArrayList<Attribute>();
		
		for (Attribute atributo : getGc().getEntity().getAttributes()) {
			if (!AttributeId.class.isInstance(atributo) && !AttributeOneToMany.class.isInstance(atributo)) {
				
				// Verifica se o atributo e anotado com @Version
				if (atributo.getField().getAnnotation(Version.class) != null) {
					println(pw, "\t\t\t\t\t\t");
					println(pw, "\t\t\t\t\t\t<h:outputLabel value=\"{0}:\" for=\"{1}\" />", atributo.getLabel(), atributo.getField().getName());
					println(pw, "\t\t\t\t\t\t<p:inputText id=\"{0}\" label=\"{1}\" value=\"#'{'cc.attrs.winFrm.objeto.{0}'}'\" disabled=\"true\" />", atributo.getField().getName(), atributo.getLabel());
					println(pw, "\t\t\t\t\t\t<p:message for=\"{0}\" display=\"icon\" />", atributo.getField().getName());
				}
				else if (atributo.isRenderForm()) {
					atributos.add(atributo);
				}
			}
		}
		
		for (Attribute atributo : atributos) {

			String atributoLabel = atributo.getLabel();
			
			String required = "true";
			
			if (atributo.getField().getAnnotation(Column.class) != null) {
				required = atributo.getField().getAnnotation(Column.class).nullable() ? "false" : "true";
			}
			else if (atributo.getField().getAnnotation(ManyToOne.class) != null) {
				required = atributo.getField().getAnnotation(ManyToOne.class).optional() ? "false" : "true";
			}
			else if (atributo.getField().getAnnotation(JoinColumn.class) != null) {
				required = atributo.getField().getAnnotation(JoinColumn.class).nullable() ? "false" : "true";
			}
			
			println(pw, "\t\t\t\t\t\t");

			println(pw, "\t\t\t\t\t\t<h:outputLabel value=\"{0}:\" for=\"{1}\" />", atributoLabel, atributo.getField().getName());
			
			if (String.class.isAssignableFrom(atributo.getField().getType())) {
				println(
						pw, 
						"\t\t\t\t\t\t<p:inputText id=\"{0}\" label=\"{1}\" value=\"#'{'cc.attrs.winFrm.objeto.{0}'}'\" style=\"width: 300px\" required=\"{2}\" />",
						atributo.getField().getName(),
						atributoLabel,
						required
				);
			}
			else if (BaseEnum.class.isAssignableFrom(atributo.getField().getType())) {				
				println(pw, "\t\t\t\t\t\t<p:selectOneMenu id=\"{0}\" label=\"{1}\" value=\"#'{'cc.attrs.winFrm.objeto.{0}'}'\" effectDuration=\"0\" required=\"{2}\">", atributo.getField().getName(), atributoLabel, required);
				println(pw, "\t\t\t\t\t\t\t<f:selectItems value=\"#'{'selectItems.{0}Itens'}'\" />", firstToLowerCase(atributo.getField().getType().getSimpleName()));
				println(pw, "\t\t\t\t\t\t</p:selectOneMenu>");
			}
			else if (Date.class.isAssignableFrom(atributo.getField().getType())) {
				if (atributo.getField().getAnnotation(Temporal.class).value() == TemporalType.DATE) {
					println(pw, "\t\t\t\t\t\t<p:calendar id=\"{0}\" label=\"{1}\" value=\"#'{'cc.attrs.winFrm.objeto.{0}'}'\" showOn=\"button\" locale=\"pt_BR\" pattern=\"dd/MM/yyyy\" required=\"{2}\" onkeypress=\"Mask.valid(this, ''data'')\" />", atributo.getField().getName(), atributoLabel, required);		    			

				}
				else if (atributo.getField().getAnnotation(Temporal.class).value() == TemporalType.TIME) {
					println(pw, "\t\t\t\t\t\t<p:calendar id=\"{0}\" label=\"{1}\" value=\"#'{'cc.attrs.winFrm.objeto.{0}'}'\" size=\"4\" showOn=\"button\" locale=\"pt_BR\" pattern=\"HH:mm\" timeOnly=\"true\" required=\"{2}\" onkeypress=\"Mask.valid(this, ''horario'')\" />", atributo.getField().getName(), atributoLabel, required);
				}
				else {
					println(pw, "\t\t\t\t\t\t<p:calendar id=\"{0}\" label=\"{1}\" value=\"#'{'cc.attrs.winFrm.objeto.{0}'}'\" showOn=\"button\" locale=\"pt_BR\" pattern=\"dd/MM/yyyy HH:mm\" required=\"{2}\" onkeypress=\"Mask.valid(this, ''dataHorario'')\" />", atributo.getField().getName(), atributoLabel, required);
				}
			}
			else if (IBaseEntity.class.isAssignableFrom(atributo.getField().getType())) {
				
				AttributeManyToOne atributoManyToOne = (AttributeManyToOne) atributo;

				// Imprime um selectOneMenu
				//println(pw, "\t\t\t\t\t\t<p:selectOneMenu id=\"{0}\" label=\"{1}\" value=\"#'{'cc.attrs.winFrm.objeto.{0}'}'\" effectDuration=\"0\" converter=\"simpleEntityConverter\">", atributo.getField().getName(), atributoLabel);
				//println(pw, "\t\t\t\t\t\t\t<f:selectItems value=\"#'{'selectItems.{0}Itens'}'\" />", firstToLowerCase(atributo.getField().getType().getSimpleName()));
				//println(pw, "\t\t\t\t\t\t</p:selectOneMenu>");
				
				Field associacaoFieldId = JpaReflectionUtils.getFieldId(atributo.getField().getType());

				// Imprime um autocomplete
				println(pw, "\t\t\t\t\t\t<p:autoComplete id=\"{0}\" label=\"{1}\" value=\"#'{'cc.attrs.winFrm.objeto.{0}'}'\" required=\"{2}\" forceSelection=\"true\"", atributo.getField().getName(), atributoLabel, required);
				println(pw, "\t\t\t\t\t\t\tdisabled=\"#'{'cc.attrs.winFrm.entidadeAssociada != null and cc.attrs.winFrm.entidadeAssociada == cc.attrs.winFrm.objeto.{0}'}'\"", atributo.getField().getName());
				println(pw, "\t\t\t\t\t\t\tcompleteMethod=\"#'{'autoCompleteCtrl.onComplete{0}'}'\" dropdown=\"true\" converter=\"lazyEntityConverter\"", atributo.getField().getType().getSimpleName());
				println(pw, "\t\t\t\t\t\t\tvar=\"{0}\" itemValue=\"#'{'{0}'}'\" itemLabel=\"#'{'{0}.{1}'}'\"", firstToLowerCase(atributo.getField().getType().getSimpleName()), atributoManyToOne.getDescriptionAttributeOfAssociation());
				println(pw, "\t\t\t\t\t\t\tsize=\"40\" scrollHeight=\"200\">");
				println(pw, "\t\t\t\t\t\t\t<p:column><h:outputText value=\"#'{'{0}.{1}'}'\" /></p:column>", firstToLowerCase(atributo.getField().getType().getSimpleName()),  associacaoFieldId.getName());
				println(pw, "\t\t\t\t\t\t\t<p:column><h:outputText value=\"#'{'{0}.{1}'}'\" /></p:column>", firstToLowerCase(atributo.getField().getType().getSimpleName()), atributoManyToOne.getDescriptionAttributeOfAssociation());
				println(pw, "\t\t\t\t\t\t</p:autoComplete>");
			}
			
			println(pw, "\t\t\t\t\t\t<p:message for=\"{0}\" display=\"icon\" />", atributo.getField().getName());
		}
		
		println(pw, "\t\t\t\t\t</h:panelGrid>");
		
		println(pw, "\t\t\t\t</p:tab>");		
	}	
}
