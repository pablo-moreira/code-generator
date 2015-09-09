package com.github.cg.task;

import java.util.List;

import com.github.cg.model.AttributeOneToMany;

import br.com.atos.cg.gui.DlgAttributeOneToMany;

public class FrmAttributeOneToManyTask extends Task {
	
	public TaskResult execute() {
		
		TaskResult taskResult = TaskResult.CONTINUE;
		
		List<AttributeOneToMany> attributesOneToMany = getTargetContext().getEntity().getAttributesOneToMany();
		
		for (AttributeOneToMany attribute : attributesOneToMany) {

			getManagerRepository().getEntityManager().loadAssociationEntity(attribute);
			        	
        	DlgAttributeOneToMany winFrmAttributeOneToMany = new DlgAttributeOneToMany(null, true);                                        
        	winFrmAttributeOneToMany.start(attribute, getTargetContext(), getTargetTask());

            if (!winFrmAttributeOneToMany.isStatusOK()) {
            	taskResult = TaskResult.STOP;
            	break;
            }
            else {
            	taskResult = TaskResult.CONTINUE;
            	getManagerRepository().getEntityManager().storeEntity(attribute.getAssociationEntity());
            }
        }
		
		return taskResult;
	}
}