package ${packageController};

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ${packageModel}.${EntityName};
import ${packageController}.AppCtrl;
import br.com.atos.faces.controller.component.Grid;
import br.com.atos.faces.view.interceptors.annotations.HandlesError;
import ${packageDAO}.${EntityName}DAO;
import ${packageWinFrm}.WinFrm${EntityName};

@Named
@ConversationScoped
@HandlesError
public class ${EntityName}${page.manager.suffix}Ctrl extends AppCtrl {

	private static final long serialVersionUID = 1L;

	private Grid<${EntityName}> grid = new Grid<${EntityName}>(${EntityName}DAO.class);

	@Inject
	private Conversation conversation;
	
	@Inject
	private WinFrm${EntityName} winFrm;
	
	@PostConstruct
    public void initializeConversation() {
        if (conversation.isTransient()) {
        	this.conversation.begin();
        }
    }

	public WinFrm${EntityName} getWinFrm() {
		return winFrm;
	}

	public void save() throws Exception {

		getWinFrm().save();
	
		getGrid().update();
	}
	
	public void delete() throws Exception {
		
		getWinFrm().delete();
		
		getGrid().update();
	}

	public Grid<${EntityName}> getGrid() {
		return grid;
	}
}