package ${packageController};

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ${packageModel}.${EntityName};
import ${packageController}.AppCtrl;
import br.com.atosdamidia.comuns.controlador.componente.Grid;
import br.com.atosdamidia.comuns.visao.interceptors.annotations.TrataErro;
import ${packageDAO}.${EntityName}DAO;
import ${packageWinFrm}.WinFrm${EntityName};

@Named
@ConversationScoped
@TrataErro
public class ${EntityName}AdministrarCtrl extends AppCtrl {

	private static final long serialVersionUID = 1L;

	private Grid<${EntityName}> grid;

	@Inject
	private Conversation conversation;
	
	@Inject
	private WinFrm${EntityName} winFrm;

	public ${EntityName}AdministrarCtrl() {
		grid = new Grid<${EntityName}>(${EntityName}DAO.class);
	}
	
	@PostConstruct
    public void inicializarConversation() {
        if (conversation.isTransient()) {
        	this.conversation.begin();
        }
    }

	public WinFrm${EntityName} getWinFrm() {
		return winFrm;
	}

	public void salvar() throws Exception {

		getWinFrm().salvar();
	
		getGrid().atualizar();
	}
	
	public void excluir() throws Exception {
		
		getWinFrm().excluir();
		
		getGrid().atualizar();
	}

	public Grid<${EntityName}> getGrid() {
		return grid;
	}
}
