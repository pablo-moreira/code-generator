package ${pacoteControlador};

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ${pacoteEntidade}.${EntidadeNome};
import ${pacoteControlador}.AppCtrl;
import br.com.atosdamidia.comuns.controlador.componente.Grid;
import br.com.atosdamidia.comuns.visao.interceptors.annotations.TrataErro;
import ${pacoteDAO}.${EntidadeNome}DAO;
import ${pacoteWinFrm}.WinFrm${EntidadeNome};

@Named
@ConversationScoped
@TrataErro
public class ${EntidadeNome}AdministrarCtrl extends AppCtrl {

	private static final long serialVersionUID = 1L;

	private Grid<${EntidadeNome}> grid;

	@Inject
	private Conversation conversation;
	
	@Inject
	private WinFrm${EntidadeNome} winFrm;

	public ${EntidadeNome}AdministrarCtrl() {
		grid = new Grid<${EntidadeNome}>(${EntidadeNome}DAO.class);
	}
	
	@PostConstruct
    public void inicializarConversation() {
        if (conversation.isTransient()) {
        	this.conversation.begin();
        }
    }

	public WinFrm${EntidadeNome} getWinFrm() {
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

	public Grid<${EntidadeNome}> getGrid() {
		return grid;
	}
}
