package br.com.atos.cg.component;

import java.io.PrintWriter;
import java.util.List;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.cg.model.AttributeFormType;
import br.com.atos.cg.model.AttributeOneToMany;
import br.com.atos.utils.StringUtils;

public class WinFrmJavaMethodsComponent extends Component {

	public WinFrmJavaMethodsComponent(CodeGenerator gc) {
		super(gc);
	}

	@Override
	public String getComponentKey() {
		return "winFrmEntityMethods";
	}

	@Override
	public void render(PrintWriter pw) {

		List<AttributeOneToMany> atributosOneToMany = getGc().getEntity().getAttributesOneToMany();
		
		if (!atributosOneToMany.isEmpty()) {
		
			println(pw, "\t@PostConstruct");
			println(pw, "\tpublic void initialize() {");
			
			for (AttributeOneToMany atributo : atributosOneToMany) {						
				
				println(pw, "\t\t");
				
				if (AttributeFormType.INTERNAL.equals(atributo.getFormType())) {
					println(pw, "\t\tthis.association{2} = new FrmAssociationOneToMany<WinFrm{0}, {0}, {1}>(this, {1}.class, {1}Manager.class, \"tbView:dt{2}\") '{'", getGc().getAttributeValue("EntityName"), atributo.getAssociationClassSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));				
				}
				else {	
					println(pw, "\t\tthis.association{2} = new WinFrmAssociationOneToMany<WinFrm{0}, WinFrm{1}, {0}, {1}>(this, winFrm{1}, \"tbView:dt{2}\") '{'", getGc().getAttributeValue("EntityName"), atributo.getAssociationClassSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
				}
				
				println(pw, "\t\t\t");
				println(pw, "\t\t\t@Override");
				println(pw, "\t\t\tpublic void connect({0} association, {1} entity) '{'", atributo.getAssociationClassSimpleName(), getGc().getAttributeValue("EntityName"));			
				println(pw, "\t\t\t\tassociation.set{0}(entity);", StringUtils.firstToUpperCase(atributo.getAssociationMappedBy()));
				println(pw, "\t\t\t}");
				println(pw, "\t\t\t");
				println(pw, "\t\t\t@Override");
				println(pw, "\t\t\tpublic List<{0}> getAssociations({1} entity) '{'", atributo.getAssociationClassSimpleName(), getGc().getAttributeValue("EntityName"));
				println(pw, "\t\t\t\treturn entity.get{0}();", StringUtils.firstToUpperCase(atributo.getField().getName()));
				println(pw, "\t\t\t}");
				println(pw, "\t\t};");
			}
			
			println(pw, "\t}");
			println(pw, "\t");
			
			for (AttributeOneToMany atributo : atributosOneToMany) {
				if (AttributeFormType.INTERNAL.equals(atributo.getFormType())) {
					println(pw, "\tpublic FrmAssociationOneToMany<WinFrm{0}, {0}, {1}> getAssociation{2}() '{'", getGc().getAttributeValue("EntityName"), atributo.getAssociationClassSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
					println(pw, "\t\treturn association{0};", StringUtils.firstToUpperCase(atributo.getField().getName()));
					println(pw, "\t}");
				}
				else {
					println(pw, "\tpublic WinFrmAssociationOneToMany<WinFrm{0}, WinFrm{1}, {0}, {1}> getAssociation{2}() '{'", getGc().getAttributeValue("EntityName"), atributo.getAssociationClassSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
					println(pw, "\t\treturn association{0};", StringUtils.firstToUpperCase(atributo.getField().getName()));
					println(pw, "\t}");
				}
			}
		}		
	}
}