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
		
		VelocityContext context = createContext(target, entity, cg.getComponents(), cg.getApp(), cg.getMessagesProperties(), cg.getAttributesValues());
		
		TargetContext targetContext = new TargetContext(target, entity, context);
				
		return targetContext;
	}
	
	private VelocityContext createContext(Target target, Entity entity, HashMap<String,Object> components, HashMap<String, Object> app, LinkedProperties messagesProperties, HashMap<String,String> attributesValues) {

		VelocityContext context = new VelocityContext();
		
		context.put("entity", entity);
		context.put("target", target);

		// Components
		for (String componentName : components.keySet()) {
			context.put(componentName, components.get(componentName));
		}
				
		context.put("app", app);
		context.put("msgs", messagesProperties);

		// Atributes Values
		for (String key : attributesValues.keySet()) {
			context.put(key, attributesValues.get(key));
		}
		
		return context;
	}
}
