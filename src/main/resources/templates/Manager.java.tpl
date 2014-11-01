package ${packageManager};

import ${packageManager}.AppManager;
import br.com.atos.core.Repository;
import ${packageDAO}.${EntityName}DAO;
import ${packageModel}.${EntityName};

public class ${EntityName}Manager extends AppManager<${EntityName}DAO, ${EntityName}, ${entityIdClass}> {

	public ${EntityName}Manager(Repository repository) {
		super(${EntityName}DAO.class, repository);
	}
}