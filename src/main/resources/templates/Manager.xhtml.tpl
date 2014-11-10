<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:f="http://xmlns.jcp.org/jsf/core"
    xmlns:h="http://xmlns.jcp.org/jsf/html"
    xmlns:ui="http://xmlns.jcp.org/jsf/facelets"
    xmlns:cc="http://xmlns.jcp.org/jsf/composite"    
    xmlns:c="http://xmlns.jcp.org/jsp/jstl/core"
    xmlns:p="http://primefaces.org/ui"
    xmlns:atos="http://xmlns.jcp.org/jsf/composite/components/atos"
    xmlns:app="http://xmlns.jcp.org/jsf/composite/components/app">
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