package com.github.cg.task;

import com.github.cg.gui.DlgFrmEntity;

public class FrmEntityTask extends Task {
	
	@Override
	public TaskResult execute() {
		
		TaskResult taskResult;
		
		DlgFrmEntity winFrm = new DlgFrmEntity(null, true, getTargetContext().getCg().getPatterns(), getTargetContext().getCg().getFormTypes());
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