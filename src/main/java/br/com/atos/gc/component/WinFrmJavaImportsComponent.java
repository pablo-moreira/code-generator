package br.com.atos.gc.component;

import java.io.PrintWriter;
import java.util.List;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.model.AttributeFormType;
import br.com.atos.gc.model.AttributeOneToMany;

public class WinFrmJavaImportsComponent extends Component {

	public WinFrmJavaImportsComponent(GeradorCodigo gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "winFrmEntidadeImports";
	}

	@Override
	public void renderizar(PrintWriter pw) {
		
		List<AttributeOneToMany> atributosOneToMany = getGc().getEntity().getAttributesOneToMany();
		
		if (atributosOneToMany.size() > 0) {
		
			println(pw, "import javax.annotation.PostConstruct;");
			println(pw, "import java.util.List;");
			
			boolean temTipoFormularioExterno = false;
			boolean temTipoFormularioEmbutido = false;
			
			for (AttributeOneToMany atributo : atributosOneToMany) {
				if (AttributeFormType.EXTERNAL.equals(atributo.getFormType())) {
					temTipoFormularioExterno = true;						
				}
				else {
					temTipoFormularioEmbutido = true;
					println(pw, "import {0}.{1}Manager;", getGc().getAtributoValor(GeradorCodigo.PACOTE_MANAGER), atributo.getAssociationClassSimpleName());
				}

				println(pw, "import {0}.{1};", getGc().getAtributoValor(GeradorCodigo.PACOTE_ENTIDADE), atributo.getAssociationClassSimpleName());
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
