package com.github.cg.manager;

import java.util.HashMap;

import org.apache.velocity.VelocityContext;

import br.com.atos.cg.CodeGenerator;
import br.com.atos.cg.util.LinkedProperties;

import com.github.cg.model.Entity;
import com.github.cg.model.Target;
import com.github.cg.model.TargetContext;

public class TargetManager extends BaseManager {

	public TargetManager(ManagerRepository repository) {
		super(repository);
	}

	public TargetContext createTargetContext(Target target, Entity entity) {

		CodeGenerator cg = getManagerRepository().getCodeGenerator();

		TargetContext targetContext = new TargetContext(target, entity);
		
		HashMap<String,Object> components = getManagerRepository().getComponentManager().createComponents(cg.getComponentsClass(), targetContext);
				
		targetContext.setContext(createContext(target, entity, components, cg.getApp(), cg.getMessagesProperties()));
				
		return targetContext;
	}
	
	private VelocityContext createContext(Target target, Entity entity, HashMap<String,Object> components, HashMap<String, Object> app, LinkedProperties messagesProperties) {

		VelocityContext context = new VelocityContext();
		
		context.put("entity", entity);
		context.put("target", target);

		// Components
		for (String componentName : components.keySet()) {
			context.put(componentName, components.get(componentName));
		}

		context.put("app", app);
		context.put("msgs", messagesProperties);

		return context;
	}
}
