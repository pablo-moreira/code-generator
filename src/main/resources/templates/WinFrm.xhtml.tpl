<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:composite="http://java.sun.com/jsf/composite"
    xmlns:p="http://primefaces.org/ui"  
    xmlns:atos="http://java.sun.com/jsf/composite/components/atos"
    xmlns:app="http://java.sun.com/jsf/composite/components/app">

	<composite:interface>
		<composite:attribute name="winFrm" required="true" type="${packageWinFrm}.WinFrm${EntityName}" />
		<composite:attribute name="saveAction" required="false" default="#{cc.attrs.winFrm.save}" method-signature="void action()" />
		<composite:attribute name="deleteAction" required="false" default="#{cc.attrs.winFrm.delete}" method-signature="void action()" />
	</composite:interface>

	<composite:implementation>
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
	</composite:implementation>
</html>