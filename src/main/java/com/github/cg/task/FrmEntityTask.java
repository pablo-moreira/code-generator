package com.github.cg.task;

import br.com.atos.cg.gui.WinFrmEntity;

public class FrmEntityTask extends Task {
	
	public TaskResult execute() {
		
		TaskResult taskResult;
		
		WinFrmEntity winFrm = new WinFrmEntity(null, true);                        
		winFrm.start(getTargetContext(), getTargetTask());
            
		if (!winFrm.isStatusOK()) {
			taskResult = TaskResult.STOP;
		}
        else {
        	getCodeGenerator().store(getTargetContext().getEntity());
        	
        	taskResult = TaskResult.CONTINUE;
        }
		
		return taskResult;
	}
}