<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:composite="http://java.sun.com/jsf/composite"
    xmlns:p="http://primefaces.org/ui"  
    xmlns:app="http://java.sun.com/jsf/composite/components/app"
    xmlns:u="http://www.atosdamidia.com.br/facelets">	
	<ui:composition template="/templates/default.xhtml">    
	   	<ui:param name="pageTitle" value="Administração de ${entityLabel}s" />
		<ui:define name="content">
			<h:form id="frm">				
				<app:grid${EntityName} grid="#{${entityName}${page.manager.suffix}Ctrl.grid}" winFrm="#{${entityName}${page.manager.suffix}Ctrl.winFrm}" />				
			</h:form>
			<app:winFrm${EntityName} winFrm="#{${entityName}${page.manager.suffix}Ctrl.winFrm}" saveAction="#{${entityName}${page.manager.suffix}Ctrl.save}" deleteAction="#{${entityName}${page.manager.suffix}Ctrl.delete}" />
		</ui:define>
	</ui:composition>
</html>