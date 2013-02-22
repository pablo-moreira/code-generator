package ${pacoteGrid};

import br.com.atosdamidia.comuns.controlador.componente.Grid;
import ${pacoteDAO}.${EntidadeNome}DAO;
import ${pacoteEntidade}.${EntidadeNome};

public class Grid${EntidadeNome} extends Grid<${EntidadeNome}>  {

	private static final long serialVersionUID = 1L;

	public Grid${EntidadeNome}() {
		super(${EntidadeNome}DAO.class);
	}
}