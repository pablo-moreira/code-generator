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
		<composite:attribute name="winFrm" required="true" type="${pacoteWinFrm}.WinFrm${EntidadeNome}" />
		<composite:attribute name="salvarAction" required="false" default="#{cc.attrs.winFrm.salvar}" method-signature="void action()" />
		<composite:attribute name="excluirAction" required="false" default="#{cc.attrs.winFrm.excluir}" method-signature="void action()" />
	</composite:interface>

	<composite:implementation>
		<atos:winFrmCrud 
			dialogoTitulo="Formulário para cadastro de ${entidadeRotulo}."
			dialogoExclusaoTitulo="Exclusão de ${entidadeRotulo}."
			dialogoExclusaoMensagem="Você tem certeza que deseja excluir ${artigoDefinido} ${entidadeRotulo}?"
			winFrmCrud="#{cc.attrs.winFrm}"
			salvarAction="#{cc.attrs.salvarAction}"
			excluirAction="#{cc.attrs.excluirAction}">			
			${winFrmXhtml}
		</atos:winFrmCrud>
		${winFrmXhtmlAssociacoes}
	</composite:implementation>
</html>