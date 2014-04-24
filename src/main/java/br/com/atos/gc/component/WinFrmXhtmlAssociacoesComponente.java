package br.com.atos.gc.component;

import java.io.PrintWriter;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.model.AttributeFormType;
import br.com.atos.gc.model.AttributeOneToMany;
import br.com.atos.utils.StringUtils;

public class WinFrmXhtmlAssociacoesComponente extends Componente {

	public WinFrmXhtmlAssociacoesComponente(GeradorCodigo gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "winFrmXhtmlAssociacoes";
	}

	@Override
	public void renderizar(PrintWriter pw) {
		for (AttributeOneToMany atributo : getGc().getEntity().getAtributosOneToMany()) {
			if (AttributeFormType.EXTERNO.equals(atributo.getFormType())) {
				println(pw, "\t\t<custom:winFrm{0} winFrm=\"#'{'cc.attrs.winFrm.associacao{1}.winFrmAssociacao'}'\" salvarAction=\"#'{'cc.attrs.winFrm.associacao{1}.salvar'}'\" excluirAction=\"#'{'cc.attrs.winFrm.associacao{1}.excluir'}'\" />", atributo.getAssociationClassSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
			}
		}
	}
}