package ${packageController};

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.atos.faces.view.interceptors.annotations.HandlesError;
import ${packageDAO}.${EntityName}DAO;
import ${packageModel}.${EntityName};

@Named
@ConversationScoped
@HandlesError
public class ${EntityName}${page.view.suffix}Ctrl extends AppCtrl {

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
	
	public void start() {
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