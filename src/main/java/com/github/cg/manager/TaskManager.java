package com.github.cg.manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.github.cg.model.TargetContext;
import com.github.cg.model.TargetTask;
import com.github.cg.task.Task;
import com.github.cg.task.TaskResult;

public class TaskManager extends BaseManager {

	private static final Logger log = Logger.getLogger(TaskManager.class);
	
	public TaskManager(ManagerRepository repository) {
		super(repository);
	}
	
	public void executeTasks(TargetContext targetContext, List<TargetTask> targetTasks) {

		for (TargetTask targetTask : targetTasks) {
			
			Task task = createTaskInstance(targetContext, targetTask);
			
			if (task != null) {
				
				TaskResult result = task.execute();
				
				if (TaskResult.CONTINUE != result) {
					return;
				}
			}
		}
	}

	private Task createTaskInstance(TargetContext targetContext, TargetTask targetTask) {
		
		try {
			Task task = targetTask.getTask().newInstance();
			task.init(getManagerRepository(), targetContext, targetTask);
			return task;
		}
		catch (Exception e) {
			log.error("Não foi possível instanciar task!");
		}
		
		return null;
	}
}