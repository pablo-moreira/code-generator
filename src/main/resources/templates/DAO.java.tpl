package ${pacoteDAO};

import javax.persistence.EntityManager;

import ${pacoteDAO}.AppDAO;
import ${pacoteEntidade}.${EntidadeNome};

public class ${EntidadeNome}DAO extends AppDAO<${EntidadeNome}, ${entidadeIdClass}> {

	public ${EntidadeNome}DAO(EntityManager entityManager) {
		super(entityManager, ${EntidadeNome}.class);
	}
}