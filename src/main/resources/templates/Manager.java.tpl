package ${pacoteManager};

import ${pacoteManager}.AppManager;
import br.com.atosdamidia.comuns.modelo.Repositorio;
import ${pacoteDAO}.${EntidadeNome}DAO;
import ${pacoteEntidade}.${EntidadeNome};

public class ${EntidadeNome}Manager extends AppManager<${EntidadeNome}DAO, ${EntidadeNome}, ${entidadeIdClass}> {

	public ${EntidadeNome}Manager(Repositorio repositorio) {
		super(${EntidadeNome}DAO.class, repositorio);
	}
}