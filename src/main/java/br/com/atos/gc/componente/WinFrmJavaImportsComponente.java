package br.com.atos.gc.componente;

import java.io.PrintWriter;
import java.util.List;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.modelo.AtributoOneToMany;
import br.com.atos.gc.modelo.AtributoTipoFormulario;

public class WinFrmJavaImportsComponente extends Componente {

	public WinFrmJavaImportsComponente(GeradorCodigo gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "winFrmEntidadeImports";
	}

	@Override
	public void renderizar(PrintWriter pw) {
		
		List<AtributoOneToMany> atributosOneToMany = getGc().getEntidade().getAtributosOneToMany();
		
		if (atributosOneToMany.size() > 0) {
		
			println(pw, "import javax.annotation.PostConstruct;");
			println(pw, "import java.util.List;");
			
			boolean temTipoFormularioExterno = false;
			boolean temTipoFormularioEmbutido = false;
			
			for (AtributoOneToMany atributo : atributosOneToMany) {
				if (AtributoTipoFormulario.EXTERNO.equals(atributo.getTipoFormulario())) {
					temTipoFormularioExterno = true;						
				}
				else {
					temTipoFormularioEmbutido = true;
					println(pw, "import {0}.{1}Manager;", getGc().getAtributoValor(GeradorCodigo.PACOTE_MANAGER), atributo.getAssociacaoTipoSimpleName());
				}

				println(pw, "import {0}.{1};", getGc().getAtributoValor(GeradorCodigo.PACOTE_ENTIDADE), atributo.getAssociacaoTipoSimpleName());
			}
			
			if (temTipoFormularioExterno) {
				println(pw, "import javax.enterprise.inject.New;");
				println(pw, "import javax.inject.Inject;");
				println(pw, "import br.com.atosdamidia.comuns.controlador.componente.WinFrmAssociacaoOneToMany;");
			}
			
			if (temTipoFormularioEmbutido) {
				println(pw, "import br.com.atosdamidia.comuns.controlador.componente.FrmAssociacaoOneToMany;");
			}
		}		
	}
}
