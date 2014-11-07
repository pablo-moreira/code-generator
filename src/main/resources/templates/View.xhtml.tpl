<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:composite="http://java.sun.com/jsf/composite"
    xmlns:p="http://primefaces.org/ui"  
    xmlns:app="http://java.sun.com/jsf/composite/components/app"
    xmlns:u="http://www.atosdamidia.com.br/facelets">
    <f:metadata>
		<f:viewParam name="id" value="#{${entityName}${page.view.suffix}Ctrl.id}" required="true" />
		<f:event type="preRenderView" listener="#{${entityName}${page.view.suffix}Ctrl.start}" />
	</f:metadata>
	<ui:composition template="/templates/default.xhtml">    
	   	<ui:param name="pageTitle" value="Visualizando ${gender} ${entityLabel}" />
		<ui:define name="content">
			<h:form id="frm">
				<p:tabView>					
					${viewXhtml}
				</p:tabView>
			</h:form>
		</ui:define>
	</ui:composition>
</html>