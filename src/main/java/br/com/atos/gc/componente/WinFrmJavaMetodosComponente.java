package br.com.atos.gc.componente;

import java.io.PrintWriter;
import java.util.List;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.modelo.AtributoOneToMany;
import br.com.atos.gc.modelo.AtributoTipoFormulario;
import br.com.atos.utils.StringUtils;

public class WinFrmJavaMetodosComponente extends Componente {

	public WinFrmJavaMetodosComponente(GeradorCodigo gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "winFrmEntidadeMetodos";
	}

	@Override
	public void renderizar(PrintWriter pw) {

		List<AtributoOneToMany> atributosOneToMany = getGc().getEntidade().getAtributosOneToMany();
		
		if (!atributosOneToMany.isEmpty()) {
		
			println(pw, "\t@PostConstruct");
			println(pw, "\tpublic void initAssociacoes() {");
			
			for (AtributoOneToMany atributo : atributosOneToMany) {						
				
				println(pw, "\t\t");
				
				if (AtributoTipoFormulario.INTERNO.equals(atributo.getTipoFormulario())) {
					println(pw, "\t\tthis.associacao{2} = new FrmAssociacaoOneToMany<WinFrm{0}, {0}, {1}>(this, {1}.class, {1}Manager.class, \"tbView:dt{2}\") '{'", getGc().getAtributoValor("EntidadeNome"), atributo.getAssociacaoTipoSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));				
				}
				else {	
					println(pw, "\t\tthis.associacao{2} = new WinFrmAssociacaoOneToMany<WinFrm{0}, WinFrm{1}, {0}, {1}>(this, winFrm{1}, \"tbView:dt{2}\") '{'", getGc().getAtributoValor("EntidadeNome"), atributo.getAssociacaoTipoSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
				}
				
				println(pw, "\t\t\t");
				println(pw, "\t\t\t@Override");
				println(pw, "\t\t\tpublic void associar({0} associacao, {1} entidade) '{'", atributo.getAssociacaoTipoSimpleName(), getGc().getAtributoValor("EntidadeNome"));			
				println(pw, "\t\t\t\tassociacao.set{0}(entidade);", atributo.getAssociacaoMappedBy());
				println(pw, "\t\t\t}");
				println(pw, "\t\t\t");
				println(pw, "\t\t\t@Override");
				println(pw, "\t\t\tpublic List<{0}> getAssociacoes({1} entidade) '{'", atributo.getAssociacaoTipoSimpleName(), getGc().getAtributoValor("EntidadeNome"));
				println(pw, "\t\t\t\treturn entidade.get{0}();", StringUtils.firstToUpperCase(atributo.getField().getName()));
				println(pw, "\t\t\t}");
				println(pw, "\t\t};");
			}
			
			println(pw, "\t}");
			println(pw, "\t");
			
			for (AtributoOneToMany atributo : atributosOneToMany) {
				if (AtributoTipoFormulario.INTERNO.equals(atributo.getTipoFormulario())) {
					println(pw, "\tpublic FrmAssociacaoOneToMany<WinFrm{0}, {0}, {1}> getAssociacao{2}() '{'", getGc().getAtributoValor("EntidadeNome"), atributo.getAssociacaoTipoSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
					println(pw, "\t\treturn associacao{0};", StringUtils.firstToUpperCase(atributo.getField().getName()));
					println(pw, "\t}");
				}
				else {
					println(pw, "\tpublic WinFrmAssociacaoOneToMany<WinFrm{0}, WinFrm{1}, {0}, {1}> getAssociacao{2}() '{'", getGc().getAtributoValor("EntidadeNome"), atributo.getAssociacaoTipoSimpleName(), StringUtils.firstToUpperCase(atributo.getField().getName()));
					println(pw, "\t\treturn associacao{0};", StringUtils.firstToUpperCase(atributo.getField().getName()));
					println(pw, "\t}");
				}
			}
		}		
	}
}