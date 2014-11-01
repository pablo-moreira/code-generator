package br.com.atos.cg.component;

import java.io.PrintWriter;
import java.util.List;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.cg.model.AttributeFormType;
import br.com.atos.cg.model.AttributeOneToMany;

public class WinFrmJavaImportsComponent extends Component {

	public WinFrmJavaImportsComponent(CodeGenerator gc) {
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
					println(pw, "import {0}.{1}Manager;", getGc().getAttributeValue(CodeGenerator.PACKAGE_MANAGER), atributo.getAssociationClassSimpleName());
				}

				println(pw, "import {0}.{1};", getGc().getAttributeValue(CodeGenerator.PACKAGE_MODEL), atributo.getAssociationClassSimpleName());
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
