package ${packageController};

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.atosdamidia.comuns.visao.interceptors.annotations.TrataErro;
import ${packageDAO}.${EntityName}DAO;
import ${packageModel}.${EntityName};

@Named
@ConversationScoped
@TrataErro
public class ${EntityName}VisualizarCtrl extends AppCtrl {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;
	private ${EntityName} entity;
	private ${entityIdClass} id;
		
	@PostConstruct
    public void initializeConversation() {
        if (conversation.isTransient()) {
        	this.conversation.begin();
        }
    }
	
	public void initialize() {
		if (!FacesContext.getCurrentInstance().isPostback() && !FacesContext.getCurrentInstance().isValidationFailed()) {
			entity = getDAO(${EntityName}DAO.class).retrieveById(getId());
		}
	}

	public ${entityIdClass} getId() {
		return id;
	}
	
	public void setId(${entityIdClass} id) {
		this.id = id;
	}
	
	public ${EntityName} getEntity() {
		return entity;
	}
}