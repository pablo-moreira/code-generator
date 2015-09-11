package br.com.atos.cg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

import br.com.atos.utils.StringUtils;

import com.github.cg.annotation.Plugin;
import com.github.cg.annotation.Target;
import com.github.cg.annotation.TargetGroup;
import com.github.cg.annotation.TargetTask;
import com.github.cg.annotation.TaskConfig;

public class CodeGeneratorInitializer {

	private static final Logger log = Logger.getLogger(CodeGeneratorInitializer.class);
	
	private CodeGenerator cg;
	private Reflections reflections;
	
	public CodeGeneratorInitializer(CodeGenerator cg) {
		this.cg = cg;		
	}
	
	/**
	 * Inicializa o gerador de codigo
	 */
	public void init() {
		
		this.reflections = new Reflections();

		initEntities();
		initComponents();
		initPlugins();
	}
	
	private Reflections getReflections() {
		return reflections;
	}

	private CodeGenerator getCg() {
		return cg;
	}

	/**
	 * Recupera todas as classes anotadas com anotacao @Entity do JPA e adiciona a uma lista de entidades do gerador de codigo
	 */
	private void initEntities() {
		
		Set<Class<?>> typesAnnotatedWith = getReflections().getTypesAnnotatedWith(javax.persistence.Entity.class);
		
		for (Class<?> entityClass : typesAnnotatedWith) {			
			getCg().addEntityClass(entityClass);
		}
	}

	/**
	 * Recupera todas as classes anotadas com anotacao @Plugin 
	 * converte os objetos do tipo anotacao para objetos do tipo model e adiciona a uma lista de plugins do gerador de codigo 
	 */
	private void initPlugins() {

		Set<Class<?>> typesAnnotatedWith = getReflections().getTypesAnnotatedWith(Plugin.class);
		
		HashMap<Plugin,com.github.cg.model.Plugin> map = new HashMap<Plugin,com.github.cg.model.Plugin>();
				
		for (Class<?> clazz : typesAnnotatedWith) {
			
			Plugin pluginAnnotation = clazz.getAnnotation(Plugin.class);
			
			com.github.cg.model.Plugin plugin = new com.github.cg.model.Plugin();
			
			initPluginTargets(pluginAnnotation, plugin);
			
			getCg().addPatterns(pluginAnnotation.patterns());			
			getCg().addPlugin(plugin);
			
			map.put(pluginAnnotation, plugin);
		}
		
		// Apos carregar todos os plugins inicializa os targets groups destes plugins		
		for (Plugin pluginAnnotation : map.keySet()) {
		
			com.github.cg.model.Plugin plugin = map.get(pluginAnnotation);
			
			initPluginTargetsGroups(pluginAnnotation,plugin);			
		}			
	}
		
	/**
	 * Para cada target do tipo annotation cria um target do tipo model e
	 * adiciona a uma lista de targets do plugin
	 */	
	private void initPluginTargets(Plugin pluginAnnotation, com.github.cg.model.Plugin plugin) {
		
		for (Target targetAnnotation : pluginAnnotation.targets()) {
			
			com.github.cg.model.Target target = new com.github.cg.model.Target(
					targetAnnotation.name(),
					targetAnnotation.description(),
					targetAnnotation.allowOverwrite(),
					targetAnnotation.filename(),
					targetAnnotation.template()
			);				
			
			for (TargetTask targetTaskAnnotation : targetAnnotation.tasksToExecuteBefore()) {
				target.addTaskExecuteBefore(initTargetTask(targetTaskAnnotation));
			}
			
			for (TargetTask targetTaskAnnotation : targetAnnotation.tasksToExecuteAfter()) {
				target.addTaskExecuteAfter(initTargetTask(targetTaskAnnotation));
			}
			
			plugin.addTarget(target);
		}		
	}
	
	/**
	 * Instancia um objeto TargetTask do pacote model a partir de um objeto targetTask do pacote annotacao
	 * @param targetTask do pacote annotation
	 * @return targetTask do pacote model
	 */
	private com.github.cg.model.TargetTask initTargetTask(TargetTask targetTaskAnnotation) {

		com.github.cg.model.TargetTask targetTask = new com.github.cg.model.TargetTask(targetTaskAnnotation.task());
		
		for (TaskConfig taskConfigAnnotation : targetTaskAnnotation.configs()) {
			targetTask.getConfigs().put(taskConfigAnnotation.name(), taskConfigAnnotation.value());
		}		
		
		return targetTask;
	}

	/**
	 * Para cada target group annotation cria um target group do tipo model e
	 * adiciona a uma lista de targets groups do plugin
	 */	
	private void initPluginTargetsGroups(Plugin pluginAnnotation, com.github.cg.model.Plugin plugin) {
		
		for (TargetGroup targetGroupAnnotation : pluginAnnotation.targetsGroups()) {
			
			List<com.github.cg.model.Target> targets = new ArrayList<com.github.cg.model.Target>();
			
			boolean notfound = false;
			
			for(String targetName : targetGroupAnnotation.targets()) {
				
				com.github.cg.model.Target target = getCg().findTargetByName(targetName);
				
				if (target != null) {
					targets.add(target);
				}
				else {
					notfound = true;
					log.error("Não foi possível encontrar a target: " + targetName + " utilizada pelo target group: " + targetGroupAnnotation.name());
				}
			}				
			
			// Se algumas das targets nao for encontrada o target group nao sera utilizado
			if (notfound == false) {
				plugin.addTargetGroup(new com.github.cg.model.TargetGroup(targetGroupAnnotation.name(), targets));
			}
		}
	}
	
	/**
	 * Recupera todas as classes anotadas com anotacao @Component e adiciona a um mapa de componentes classes do gerador de codigo 
	 */
	private void initComponents() {
		
		Set<Class<?>> typesAnnotatedWith = getReflections().getTypesAnnotatedWith(com.github.cg.annotation.Component.class);
		
		for (Class<?> clazz : typesAnnotatedWith) {
		
			com.github.cg.annotation.Component compAnnotation = clazz.getAnnotation(com.github.cg.annotation.Component.class);
			
			String name = compAnnotation.name();
				
			if (StringUtils.isNullOrEmpty(name)) {			
				name = StringUtils.firstToLowerCase(clazz.getSimpleName());
			}
			
			getCg().getComponentsClass().put(name, clazz);
		}
	}
}