<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:composite="http://java.sun.com/jsf/composite"
    xmlns:p="http://primefaces.org/ui"  
    xmlns:custom="http://java.sun.com/jsf/composite/components/custom"
    xmlns:u="http://www.atosdamidia.com.br/facelets">	
	<ui:composition template="/templates/padrao.xhtml">    
	   	<ui:param name="pgTitulo" value="Administração de ${entidadeRotulo}s" />
		<ui:define name="conteudo">
			<h:form id="form">				
				<custom:grid${EntidadeNome} grid="#{${entidadeNome}AdministrarCtrl.grid}" winFrm="#{${entidadeNome}AdministrarCtrl.winFrm}" />				
			</h:form>
			<custom:winFrm${EntidadeNome} winFrm="#{${entidadeNome}AdministrarCtrl.winFrm}" salvarAction="#{${entidadeNome}AdministrarCtrl.salvar}" excluirAction="#{${entidadeNome}AdministrarCtrl.excluir}" />
		</ui:define>
	</ui:composition>
</html>