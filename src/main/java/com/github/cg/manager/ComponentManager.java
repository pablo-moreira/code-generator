package com.github.cg.manager;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.github.cg.component.Component;
import com.github.cg.model.TargetContext;

public class ComponentManager extends BaseManager {

	private static final Logger log = Logger.getLogger(ComponentManager.class);
	
	public ComponentManager(ManagerRepository repository) {
		super(repository);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String,Object> createComponents(HashMap<String,Class<?>> componentsClass, TargetContext targetContext) {
		
		HashMap<String,Object> components = new HashMap<String,Object>();
		
		for (String componentName : componentsClass.keySet()) {
			
			Class<?> componentClass = componentsClass.get(componentName);
			
			Object instance = null;
			
			if (Component.class.isAssignableFrom(componentClass)) {

				Class<? extends Component> compClass = (Class<? extends Component>) componentClass;
				
				Component component = null;
				
				try { 
					component = compClass.newInstance();
					component.initialize(targetContext);
				}
				catch (Exception e) {
					log.error("Não foi possível instanciar o component " + compClass.getName() + "!");
				}
				
				instance = component;
			}
			else {
				try {
					instance = componentClass.newInstance();
				}
				catch (Exception e) {
					log.error("Não foi possível instanciar o component " + componentClass.getName() + "!");
				}
			}
			
			if (instance != null) {
				components.put(componentName, instance);
			}		
		}
		
		return components;
	}
}