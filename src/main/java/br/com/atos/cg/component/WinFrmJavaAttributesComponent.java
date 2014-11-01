package br.com.atos.cg.component;

import java.io.PrintWriter;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.cg.model.AttributeFormType;
import br.com.atos.cg.model.AttributeOneToMany;
import br.com.atos.utils.StringUtils;

public class WinFrmJavaAttributesComponent extends Component {

	public WinFrmJavaAttributesComponent(CodeGenerator gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "winFrmEntidadeAtributos";
	}

	@Override
	public void renderizar(PrintWriter pw) {
		for (AttributeOneToMany atributo : getGc().getEntity().getAttributesOneToMany()) {
			if (AttributeFormType.INTERNAL.equals(atributo.getFormType())) {
				println(pw, "\tprivate FrmAssociacaoOneToMany<WinFrm{0}, {0}, {1}> associacao{2};", getGc().getAttributeValue("EntityName"), atributo.getAssociationClassSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
			}
			else {
				println(pw, "\t@Inject");
				println(pw, "\t@New");
				println(pw, "\tprivate WinFrm{0} winFrm{0};", atributo.getAssociationClassSimpleName());	
				println(pw, "\tprivate WinFrmAssociacaoOneToMany<WinFrm{0}, WinFrm{1}, {0}, {1}> associacao{2};", getGc().getAttributeValue("EntityName"), atributo.getAssociationClassSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
			}
		}		
	}
}
