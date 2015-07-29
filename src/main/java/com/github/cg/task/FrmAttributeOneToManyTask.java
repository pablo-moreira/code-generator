package com.github.cg.task;

import java.util.List;

import br.com.atos.cg.gui.WinFrmAttributeOneToMany;
import br.com.atos.cg.model.AttributeOneToMany;

public class FrmAttributeOneToManyTask extends Task {
	
	public TaskResult execute() {
		
		TaskResult taskResult = TaskResult.CONTINUE;
		
		List<AttributeOneToMany> attributesOneToMany = getTargetContext().getEntity().getAttributesOneToMany();
		
		for (AttributeOneToMany attribute : attributesOneToMany) {

        	attribute.initializeAssociationEntity();
        	
        	WinFrmAttributeOneToMany winFrmAttributeOneToMany = new WinFrmAttributeOneToMany(null, true);                                        
        	winFrmAttributeOneToMany.start(attribute, getTargetContext(), getTargetTask());

            if (!winFrmAttributeOneToMany.isStatusOK()) {
            	taskResult = TaskResult.STOP;
            	break;
            }
            else {
            	taskResult = TaskResult.CONTINUE;
            	getCodeGenerator().store(attribute.getAssociationEntity());
            }
        }
		
		return taskResult;
	}
}