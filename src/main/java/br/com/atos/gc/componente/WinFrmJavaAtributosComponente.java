package br.com.atos.gc.componente;

import java.io.PrintWriter;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.modelo.AtributoOneToMany;
import br.com.atos.gc.modelo.AtributoTipoFormulario;
import br.com.atos.utils.StringUtils;

public class WinFrmJavaAtributosComponente extends Componente {

	public WinFrmJavaAtributosComponente(GeradorCodigo gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "winFrmEntidadeAtributos";
	}

	@Override
	public void renderizar(PrintWriter pw) {
		for (AtributoOneToMany atributo : getGc().getEntidade().getAtributosOneToMany()) {
			if (AtributoTipoFormulario.INTERNO.equals(atributo.getTipoFormulario())) {
				println(pw, "\tprivate FrmAssociacaoOneToMany<WinFrm{0}, {0}, {1}> associacao{2};", getGc().getAtributoValor("EntidadeNome"), atributo.getAssociacaoTipoSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
			}
			else {
				println(pw, "\t@Inject");
				println(pw, "\t@New");
				println(pw, "\tprivate WinFrm{0} winFrm{0};", atributo.getAssociacaoTipoSimpleName());	
				println(pw, "\tprivate WinFrmAssociacaoOneToMany<WinFrm{0}, WinFrm{1}, {0}, {1}> associacao{2};", getGc().getAtributoValor("EntidadeNome"), atributo.getAssociacaoTipoSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
			}
		}		
	}
}
