package ${packageWinFrm};

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import br.com.atos.faces.view.interceptors.annotations.HandlesError;
import ${packageDAO}.${EntityName}DAO;
import ${packageModel}.${EntityName};
import ${packageManager}.${EntityName}Manager;
${winFrmEntityImports}

@Named
@ConversationScoped
@HandlesError
public class WinFrm${EntityName} extends AppWinFrmCrud<${EntityName},${entityIdClass}> {

	private static final long serialVersionUID = 1L;
	
	${winFrmEntityAttributes}
	public WinFrm${EntityName}() {
		super(${EntityName}.class, ${EntityName}DAO.class, ${EntityName}Manager.class, "${Gender} ${entityLabel} foi salv${gender} com sucesso!", "${Gender} ${entityLabel} foi excluíd${gender} com sucesso!");
	}
	
	${winFrmEntityMethods}
}