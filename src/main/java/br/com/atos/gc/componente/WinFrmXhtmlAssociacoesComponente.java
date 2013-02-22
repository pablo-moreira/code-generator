package br.com.atos.gc.componente;

import java.io.PrintWriter;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.modelo.AtributoOneToMany;
import br.com.atos.gc.modelo.AtributoTipoFormulario;
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
		for (AtributoOneToMany atributo : getGc().getEntidade().getAtributosOneToMany()) {
			if (AtributoTipoFormulario.EXTERNO.equals(atributo.getTipoFormulario())) {
				println(pw, "\t\t<custom:winFrm{0} winFrm=\"#'{'cc.attrs.winFrm.associacao{1}.winFrmAssociacao'}'\" salvarAction=\"#'{'cc.attrs.winFrm.associacao{1}.salvar'}'\" excluirAction=\"#'{'cc.attrs.winFrm.associacao{1}.excluir'}'\" />", atributo.getAssociacaoTipoSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
			}
		}
	}
}