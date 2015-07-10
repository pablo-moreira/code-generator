<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:f="http://xmlns.jcp.org/jsf/core"
    xmlns:h="http://xmlns.jcp.org/jsf/html"
    xmlns:ui="http://xmlns.jcp.org/jsf/facelets"
    xmlns:composite="http://xmlns.jcp.org/jsf/composite"    
    xmlns:c="http://xmlns.jcp.org/jsp/jstl/core"
    xmlns:p="http://primefaces.org/ui"
    xmlns:atos="http://xmlns.jcp.org/jsf/composite/components/atos"
    xmlns:app="http://xmlns.jcp.org/jsf/composite/components/app">

	<composite:interface>
		<composite:attribute name="grid" required="true" type="br.com.atos.faces.controller.component.Grid" />
		<composite:attribute name="winFrm" required="false" type="${packageWinFrm}.WinFrm${EntityName}" />
		<composite:attribute name="editPage" required="false" type="java.lang.String" />
	</composite:interface>

	<composite:implementation>				
		<atos:gridFilters grid="#{cc.attrs.grid}">
			${gridXhtmlFilters}
		</atos:gridFilters>
		
		<atos:grid grid="#{cc.attrs.grid}" dataExporter="#{dataExporterCtrl}">
	       	${gridXhtmlColumns}	       
			<p:column styleClass="col-acao" rendered="#{cc.attrs.winFrm != null}" exportable="false">
				<f:facet name="header"><h:outputText value="Ação" /></f:facet>
				<h:link outcome="/pages/${entityName}/${entityName}${page.view.suffix}.jsf" title="Visualizar o objeto">
					<f:param name="id" value="#{entity.id}" />
					<h:graphicImage value="/resources/img/s.gif" styleClass="link-icone ui-icon-zoomin" />
				</h:link>
	        	<p:commandLink action="#{cc.attrs.winFrm.startUpdate}" title="Alterar o objeto">
	        		<h:graphicImage value="/resources/img/s.gif" styleClass="link-icone ui-icon-pencil" />
	        		<f:setPropertyActionListener target="#{cc.attrs.winFrm.entity}" value="#{entity}" />
	        	</p:commandLink>
				<p:commandLink action="#{cc.attrs.winFrm.startDelete}" title="Excluir o objeto">
					<h:graphicImage value="/resources/img/s.gif" styleClass="link-icone ui-icon-trash" />
	        		<f:setPropertyActionListener target="#{cc.attrs.winFrm.entity}" value="#{entity}" />
	        	</p:commandLink>
	        </p:column>
	        
			<f:facet name="footer">
	        	<p:outputPanel layout="block" styleClass="action-left" rendered="#{cc.attrs.winFrm != null || cc.attrs.editPage}">
					<p:commandButton icon="ui-icon-document" value="Novo" action="#{cc.attrs.winFrm.startInsert}" />
					<p:button icon="ui-icon-document" value="Novo" outcome="#{cc.attrs.editPage}" rendered="#{cc.attrs.editPage != null}" />
				</p:outputPanel>
	        </f:facet>
		</atos:grid>
	</composite:implementation>
</html>