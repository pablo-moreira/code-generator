package ${packageGrid};

import br.com.atosdamidia.comuns.controlador.componente.Grid;
import ${packageDAO}.${EntityName}DAO;
import ${packageModel}.${EntityName};

public class Grid${EntityName} extends Grid<${EntityName}>  {

	private static final long serialVersionUID = 1L;

	public Grid${EntityName}() {
		super(${EntityName}DAO.class);
	}
}