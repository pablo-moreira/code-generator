<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:composite="http://java.sun.com/jsf/composite"
    xmlns:p="http://primefaces.org/ui"  
    xmlns:custom="http://java.sun.com/jsf/composite/components/custom"
    xmlns:u="http://www.atosdamidia.com.br/facelets">
    <f:metadata>
		<f:viewParam name="id" value="#{${entidadeNome}VisualizarCtrl.id}" required="true" />
		<f:event type="preRenderView" listener="#{${entidadeNome}VisualizarCtrl.iniciar}" />
	</f:metadata>
	<ui:composition template="/templates/padrao.xhtml">    
	   	<ui:param name="pgTitulo" value="Visualizando ${artigoDefinido} ${entidadeRotulo}" />
		<ui:define name="conteudo">
			<h:form id="form">
				<p:tabView dynamic="true" cache="false">								
					${visualizarXhtml}
				</p:tabView>
			</h:form>
		</ui:define>
	</ui:composition>
</html>