package com.github.cg.task;

import br.com.atos.cg.gui.DlgFrmEntity;

public class FrmEntityTask extends Task {
	
	public TaskResult execute() {
		
		TaskResult taskResult;
		
		DlgFrmEntity winFrm = new DlgFrmEntity(null, true);                        
		winFrm.start(getTargetContext(), getTargetTask());
            
		if (!winFrm.isStatusOK()) {
			taskResult = TaskResult.STOP;
		}
        else {
        	getManagerRepository().getEntityManager().storeEntity(getTargetContext().getEntity());
        	
        	taskResult = TaskResult.CONTINUE;
        }
		
		return taskResult;
	}
}