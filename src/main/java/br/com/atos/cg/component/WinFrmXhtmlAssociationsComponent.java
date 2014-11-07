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
	public String getComponentKey() {
		return "winFrmXhtmlAssociations";
	}

	@Override
	public void render(PrintWriter pw) {
		for (AttributeOneToMany attribute : getGc().getEntity().getAttributesOneToMany()) {
			if (AttributeFormType.EXTERNAL.equals(attribute.getFormType())) {
				println(pw, "\t\t<custom:winFrm{0} winFrm=\"#'{'cc.attrs.winFrm.association{1}.winFrmAssociation'}'\" saveAction=\"#'{'cc.attrs.winFrm.association{1}.save'}'\" deleteAction=\"#'{'cc.attrs.winFrm.association{1}.delete'}'\" />", attribute.getAssociationClassSimpleName(), StringUtils.firstToUpperCase(attribute.getField().getName()));
			}
		}
	}
}