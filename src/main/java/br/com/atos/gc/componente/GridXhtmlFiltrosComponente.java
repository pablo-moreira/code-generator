package br.com.atos.gc.componente;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.gc.modelo.Atributo;
import br.com.atos.gc.modelo.AtributoManyToOne;
import br.com.atos.gc.modelo.AtributoOneToMany;
import br.com.atosdamidia.componente.model.FilterDate;
import br.com.atosdamidia.comuns.modelo.BaseEnum;

public class GridXhtmlFiltrosComponente extends Componente {

	public GridXhtmlFiltrosComponente(GeradorCodigo gc) {
		super(gc);
	}

	@Override
	public String getComponenteChave() {
		return "gridXhtmlFiltros";
	}

	@Override
	public void renderizar(PrintWriter pw) {

		for (Atributo atributo : getGc().getEntidade().getAtributos()) {
			
			// Ignora as associacoes OneToMany e atributo com campo enum
			if (!AtributoOneToMany.class.isInstance(atributo)) {
				
				if (AtributoManyToOne.class.isInstance(atributo)) {
					AtributoManyToOne atributoManyToOne =  (AtributoManyToOne) atributo;
					imprimirFiltro(pw, atributo, atributoManyToOne.getAssociacaoAtributoField(), atributo.getField().getName() + "." + atributoManyToOne.getAssociacaoAtributoDescricao());
				}
				else {				
					imprimirFiltro(pw, atributo, atributo.getField(), atributo.getField().getName());
				}
			}
		}
	}
	
	private void imprimirFiltro(PrintWriter pw, Atributo atributo, Field field, String atributoPath) {

		if (!BaseEnum.class.isAssignableFrom(field.getType())) {		
		
			if (Long.class.isAssignableFrom(field.getType()) || Integer.class.isAssignableFrom(field.getType())) {				
				println(pw, "\t\t\t\t<atos:filterNumeric operatorDefault=\"=\" attribute=\"{0}\" label=\"{1}\" type=\"{2}\" />", atributoPath, atributo.getRotulo(), field.getType().getSimpleName());	
			}
			else if (Date.class.isAssignableFrom(field.getType())) {
		
				if (field.getAnnotation(Temporal.class).value() == TemporalType.DATE) {
					println(pw, "\t\t\t\t<atos:filterDate operatorDefault=\"contains\" attribute=\"{0}\" label=\"{1}\" datePattern=\"{2}\" />", atributoPath, atributo.getRotulo(), FilterDate.DATE_PATTERN_DATA);
				}
				else if (field.getAnnotation(Temporal.class).value() == TemporalType.TIMESTAMP) {
					println(pw, "\t\t\t\t<atos:filterDate operatorDefault=\"contains\" attribute=\"{0}\" label=\"{1}\" datePattern=\"{2}\" />", atributoPath, atributo.getRotulo(), FilterDate.DATE_PATTERN_DATA_HORARIO);
				}
				else {
					// Nao faz nada...
				}
			}
			else {
				println(pw, "\t\t\t\t<atos:filterString operatorDefault=\"contains\" attribute=\"{0}\" label=\"{1}\" />", atributoPath, atributo.getRotulo());
			}
		}
	}
}
