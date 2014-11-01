package ${packageWinFrm};

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import br.com.atosdamidia.comuns.controlador.componente.WinFrmCrud;
import br.com.atosdamidia.comuns.visao.interceptors.annotations.TrataErro;
import ${packageDAO}.${EntityName}DAO;
import ${packageModel}.${EntityName};
import ${packageManager}.${EntityName}Manager;
${winFrmEntidadeImports}

@Named
@ConversationScoped
@TrataErro
public class WinFrm${EntityName} extends WinFrmCrud<${EntityName},${entityIdClass}> {

	private static final long serialVersionUID = 1L;
	
	${winFrmEntidadeAtributos}
	public WinFrm${EntityName}() {
		super(${EntityName}.class, ${EntityName}DAO.class, ${EntityName}Manager.class, "${ArtigoDefinido} ${entidadeRotulo} foi salv${artigoDefinido} com sucesso!", "${ArtigoDefinido} ${entidadeRotulo} foi excluíd${artigoDefinido} com sucesso!");
	}
	
	${winFrmEntidadeMetodos}
}