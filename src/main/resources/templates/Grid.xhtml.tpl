<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:composite="http://java.sun.com/jsf/composite"
    xmlns:p="http://primefaces.org/ui"  
    xmlns:atos="http://java.sun.com/jsf/composite/components/atos"
    xmlns:custom="http://java.sun.com/jsf/composite/components/custom">

	<composite:interface>
		<composite:attribute name="grid" required="true" type="br.com.atosdamidia.comuns.controlador.componente.Grid" />
		<composite:attribute name="winFrm" required="false" type="${packageWinFrm}.WinFrm${EntityName}" />
	</composite:interface>

	<composite:implementation>
		<p:outputPanel layout="block" styleClass="form-menu" rendered="#{cc.attrs.winFrm != null}">
			<p:commandButton icon="ui-icon-document" value="Novo" action="#{cc.attrs.winFrm.iniciarInsercao}" />
		</p:outputPanel>
					
		<atos:gridFilters grid="#{cc.attrs.grid}">
			${gridXhtmlFiltros}			
		</atos:gridFilters>
		
		<atos:grid grid="#{cc.attrs.grid}" dataExporter="#{dataExporterCtrl}">
	       	${gridXhtmlColunas}	       
			<p:column styleClass="col-acao" rendered="#{cc.attrs.winFrm != null}" exportable="false">
				<f:facet name="header"><h:outputText value="Ação" /></f:facet>
				<h:link outcome="/pages/${EntityName}/${EntityName}Visualizar.jsf" title="Visualizar o objeto">
					<f:param name="id" value="#{objeto.id}" />
					<h:graphicImage value="/resources/img/s.gif" styleClass="link-icone ui-icon-zoomin" />
				</h:link>
	        	<p:commandLink action="#{cc.attrs.winFrm.iniciarEdicao}" title="Alterar o objeto">
	        		<h:graphicImage value="/resources/img/s.gif" styleClass="link-icone ui-icon-pencil" />
	        		<f:setPropertyActionListener target="#{cc.attrs.winFrm.objeto}" value="#{objeto}" />
	        	</p:commandLink>
				<p:commandLink action="#{cc.attrs.winFrm.iniciarExclusao}" title="Excluir o objeto">
					<h:graphicImage value="/resources/img/s.gif" styleClass="link-icone ui-icon-trash" />
	        		<f:setPropertyActionListener target="#{cc.attrs.winFrm.objeto}" value="#{objeto}" />
	        	</p:commandLink>				        
	        </p:column>
		</atos:grid>
	</composite:implementation>
</html>