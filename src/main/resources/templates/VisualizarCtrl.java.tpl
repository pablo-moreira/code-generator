package ${pacoteControlador};

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.atosdamidia.comuns.visao.interceptors.annotations.TrataErro;
import ${pacoteDAO}.${EntidadeNome}DAO;
import ${pacoteEntidade}.${EntidadeNome};

@Named
@ConversationScoped
@TrataErro
public class ${EntidadeNome}VisualizarCtrl extends AppCtrl {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;
	private ${EntidadeNome} entidade;
	private ${entidadeIdClass} id;
		
	@PostConstruct
    public void inicializarConversation() {
        if (conversation.isTransient()) {
        	this.conversation.begin();
        }
    }
	
	public void iniciar() {
		if (!FacesContext.getCurrentInstance().isPostback() && !FacesContext.getCurrentInstance().isValidationFailed()) {
			entidade = getDAO(${EntidadeNome}DAO.class).recuperarPorId(getId());
		}
	}

	public ${entidadeIdClass} getId() {
		return id;
	}
	
	public void setId(${entidadeIdClass} id) {
		this.id = id;
	}
	
	public ${EntidadeNome} getEntidade() {
		return entidade;
	}
}