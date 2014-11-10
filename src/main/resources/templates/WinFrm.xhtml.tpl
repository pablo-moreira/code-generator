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

	<cc:interface>
		<cc:attribute name="winFrm" required="true" type="${packageWinFrm}.WinFrm${EntityName}" />
		<cc:attribute name="saveAction" required="false" default="#{cc.attrs.winFrm.save}" method-signature="void action()" />
		<cc:attribute name="deleteAction" required="false" default="#{cc.attrs.winFrm.delete}" method-signature="void action()" />
	</cc:interface>

	<cc:implementation>
		<atos:winFrmCrud 
			dialogTitle="Formulário para cadastro de ${entityLabel}."
			dialogDeleteTitle="Exclusão de ${entityLabel}."
			dialogDeleteMessage="Você tem certeza que deseja excluir ${gender} ${entityLabel}?"
			winFrmCrud="#{cc.attrs.winFrm}"
			saveAction="#{cc.attrs.saveAction}"
			deleteAction="#{cc.attrs.deleteAction}">			
			${winFrmXhtml}
		</atos:winFrmCrud>
		${winFrmXhtmlAssociations}
	</cc:implementation>
</html>