package br.com.atos.cg.component;

import java.io.PrintWriter;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.cg.model.AttributeFormType;
import br.com.atos.cg.model.AttributeOneToMany;
import br.com.atos.utils.StringUtils;

public class WinFrmXhtmlAssociationsComponent extends Component {

	public WinFrmXhtmlAssociationsComponent(CodeGenerator gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "winFrmXhtmlAssociacoes";
	}

	@Override
	public void renderizar(PrintWriter pw) {
		for (AttributeOneToMany atributo : getGc().getEntity().getAttributesOneToMany()) {
			if (AttributeFormType.EXTERNAL.equals(atributo.getFormType())) {
				println(pw, "\t\t<custom:winFrm{0} winFrm=\"#'{'cc.attrs.winFrm.associacao{1}.winFrmAssociacao'}'\" salvarAction=\"#'{'cc.attrs.winFrm.associacao{1}.salvar'}'\" excluirAction=\"#'{'cc.attrs.winFrm.associacao{1}.excluir'}'\" />", atributo.getAssociationClassSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
			}
		}
	}
}