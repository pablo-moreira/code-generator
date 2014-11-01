package ${packageDAO};

import javax.persistence.EntityManager;

import ${packageDAO}.AppDAO;
import ${packageModel}.${EntityName};

public class ${EntityName}DAO extends AppDAO<${EntityName}, ${entityIdClass}> {

	public ${EntityName}DAO(EntityManager entityManager) {
		super(entityManager, ${EntityName}.class);
	}
}