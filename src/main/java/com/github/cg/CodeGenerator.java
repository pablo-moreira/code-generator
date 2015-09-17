package com.github.cg;

import java.io.File;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.github.cg.gui.FrmCodeGeneration;
import com.github.cg.manager.CgPropertiesManager;
import com.github.cg.manager.ManagerRepository;
import com.github.cg.model.Entity;
import com.github.cg.model.Plugin;
import com.github.cg.model.Target;
import com.github.cg.model.TargetContext;
import com.github.cg.model.TargetGroup;
import com.github.cg.util.LinkedProperties;
import com.github.cg.util.OsUtil;

public class CodeGenerator {
	
	private static final Logger log = Logger.getLogger(CodeGenerator.class);

	public static final String APP_NAME = "Code Generator";
	public static final String APP_VERSION = "1.0.0-SNAPSHOT";
	public static final String APP_TITLE = APP_NAME + " - " + APP_VERSION;

	private File dirSrc;	
	private File dirBase;
	private File dirResources;
	private File dirWebContent;
	
	private LinkedProperties cgProperties;
	private LinkedProperties messagesProperties;	
	private HashMap<String, Object> app;
	
	private List<Class<?>> entitiesClass = new ArrayList<Class<?>>();
	private HashMap<String,Class<?>> componentsClass = new HashMap<String,Class<?>>();	
	private List<Plugin> plugins = new ArrayList<Plugin>();
	private List<String> patterns = new ArrayList<String>();
	private List<String> requiredProperties = new ArrayList<String>();
	private List<String> formTypes = new ArrayList<String>();
	private ManagerRepository managerRepository = new ManagerRepository(this);
		
	@SuppressWarnings("unchecked")
	private void createAppItem(String key, String value) {
		
		String[] split = key.split("\\.");
		
		HashMap<String,Object> parent = this.app;
		
		for (int i=0; i < split.length - 1; i++) {
			
			HashMap<String,Object> child;
			
			if (!parent.containsKey(split[i])) {
				child = new HashMap<String,Object>();
				parent.put(split[i], child);				
			}
			else {
				child = (HashMap<String, Object>) parent.get(split[i]);
			}
			
			parent = child;
		}
		
		int last = split.length - 1;
		
		parent.put(split[last], value);
	}
	
	/**
	 * Exemplo de utilizacao do gerador:
	 *	CodeGenerator cg = new CodeGenerator();
	 *	cg.start();
	 * @throws Exception 
	 */
	public CodeGenerator() throws Exception {
		
		dirBase = new File(System.getProperty("user.dir"));
		
		app = new HashMap<String, Object>();

		CodeGeneratorInitializer initializer = new CodeGeneratorInitializer(this);
		initializer.init();
		
		createAppItem("dirs.base", dirBase.getAbsolutePath());
		
		loadCgProperties();
		
		Iterator<Entry<String, String>> iterator = cgProperties.iterator();
		
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			createAppItem(entry.getKey(), entry.getValue());
		}
		
		dirSrc = new File(dirBase, cgProperties.getProperty(CgPropertiesManager.DIRS_SRC));
		dirResources = new File(dirBase,  cgProperties.getProperty(CgPropertiesManager.DIRS_RESOURCES));
		dirWebContent = new File(dirBase, cgProperties.getProperty(CgPropertiesManager.DIRS_WEBCONTENT));
		
		// Verifica se os diretorios existe se nao existir mostrar msg de erro
		if (!dirSrc.exists() || !dirSrc.isDirectory()) {
			JOptionPane.showMessageDialog(null, MessageFormat.format("O diretório de codigo fonte ({0}) configurado no arquivo cg.properties é inválido!", cgProperties.getProperty(CgPropertiesManager.DIRS_SRC)), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		if (!dirWebContent.exists() || !dirWebContent.isDirectory()) {
			JOptionPane.showMessageDialog(null, MessageFormat.format("O diretório de recursos ({0}) configurado no arquivo cg.properties é inválido!", cgProperties.getProperty(CgPropertiesManager.DIRS_RESOURCES)), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		if (!dirWebContent.exists() || !dirWebContent.isDirectory()) {
			JOptionPane.showMessageDialog(null, MessageFormat.format("O diretório do conteudo WEB ({0}) configurado no arquivo cg.properties é inválido!", cgProperties.getProperty(CgPropertiesManager.DIRS_WEBCONTENT)), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		loadMessagesProperties();

        try {
            if (OsUtil.isOsLinux()) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            }
            else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }  
        }
        catch (Exception e) {}
	}

	private void loadMessagesProperties() throws Exception {		
		this.messagesProperties = getManagerRepository().getMessagesPropertiesManager().loadMessagesProperties(this.dirResources);
	}
	
	private void loadCgProperties() throws Exception {
		this.cgProperties = getManagerRepository().getCgPropertiesManager().loadCgProperties(getRequiredProperties());
	}

	/* TODO - Refatorar retirar essa responsabilidade do CodeGenerator e passar para um manager */
	public void store(Entity entity) {
		try {			
			getManagerRepository().getEntityManager().writeEntity(entity, getCgProperties(), getMessagesProperties());
						
			this.cgProperties = getManagerRepository().getCgPropertiesManager().storeAndLoad(getCgProperties(), this.dirResources);
			this.messagesProperties = getManagerRepository().getMessagesPropertiesManager().storeAndLoad(getMessagesProperties(), this.dirResources);
		}
		catch (Exception e) {
			throw new RuntimeException("Erro ao gravar os arquivos cg.properties e messages.properties, msg interna: " + e.getMessage());
		}
	}
	
	protected void print(PrintWriter pw, String string) {
		pw.print(string);
	}
	
	protected void println(PrintWriter pw, String string) {
		pw.println(string);
	}
	
	protected void println(PrintWriter pw, String string, Object ... attr) {
		pw.println(MessageFormat.format(string, attr));
	}
		
	public void start() throws Exception {
		FrmCodeGeneration winFrm = new FrmCodeGeneration(this);
		winFrm.start();
	}
	
	public LinkedProperties getCgProperties() {
		return cgProperties;
	}

	public LinkedProperties getMessagesProperties() {
		return messagesProperties;
	}
			
	public List<Class<?>> getEntitiesClass() {
		return entitiesClass;
	}

	public List<Plugin> getPlugins() {
		return plugins;
	}
	
	public List<Target> getTargets() {
		
		List<Target> targets = new ArrayList<Target>();
		
		for (Plugin plugin : getPlugins()) {
			targets.addAll(plugin.getTargets());
		}
		
		return targets;
	}
	
	public List<TargetGroup> getTargetsGroup() {
		
		List<TargetGroup> targetsGroup = new ArrayList<TargetGroup>();
		
		for (Plugin plugin : getPlugins()) {
			targetsGroup.addAll(plugin.getTargetsGroups());
		}
		
		return targetsGroup;
	}

	protected void addEntityClass(Class<?> entityClass) {
		getEntitiesClass().add(entityClass);		
	}
	
	protected void addPlugin(Plugin plugin) {
		getPlugins().add(plugin);
	}

	public Target findTargetByName(String targetName) {

		for (Target target : getTargets()) {
			if (target.getName().equals(targetName)) {
				return target;
			}
		}
		
		return null;
	}

	public ManagerRepository getManagerRepository() {
		return managerRepository;
	}

	public void execute(Class<?> entityClass, Target target) {
		
		Entity entity = getManagerRepository().getEntityManager().loadEntity(entityClass, getCgProperties(), getMessagesProperties());
		
		TargetContext targetContext = getManagerRepository().getTargetManager().createTargetContext(target, entity);
		
		String filename = getManagerRepository().getTemplateManager().mergeTemplateString(targetContext.getContext(), target.getFilenameTemplate());
		
		File file = new File(filename);
		
		targetContext.setFile(file);
		
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
				
		// Verifica se o arquivo existe
		if (file.exists()) {

			if (!target.isAllowOverwrite()) {
				log.warn(file.getName() + " [IGNORADO]");				
				return;
			}
			else {
				int result = JOptionPane.showConfirmDialog(null, "Você tem certeza que deseja substituir o arquivo: " + file.getName(), "Substituir arquivo",JOptionPane.YES_NO_OPTION);
			
				if (JOptionPane.YES_OPTION != result) {
					log.warn(file.getName() + " [IGNORADO]");					
					return;
				}			
			}
		}
		
		getManagerRepository().getTaskManager().executeTasks(targetContext, target.getTasksToExecuteBefore());
				
		getManagerRepository().getTemplateManager().mergeTemplateFile(targetContext.getContext(), target.getTemplate(), file);
		
		getManagerRepository().getTaskManager().executeTasks(targetContext, target.getTasksToExecuteAfter());

        log.info(file.getName() + " [GERADO]");
	}

	public HashMap<String, Object> getApp() {
		return app;
	}

	public void executeTargetByName(String targetName, Class<?> entityClass) {
		
		if (!getEntitiesClass().contains(entityClass)) {
			throw new RuntimeException(MessageFormat.format("A classe: {0} não é @Entity!", entityClass.getSimpleName()));
		}
		
		Target target = findTargetByName(targetName);
		
		if (target == null) {
			throw new RuntimeException(MessageFormat.format("O target: {0} não foi encontrado!", targetName));
		}
		
		execute(entityClass, target);
	}

	public HashMap<String, Class<?>> getComponentsClass() {
		return componentsClass;
	}

	/**
	 * Adiciona uma lista de patterns na lista de patterns do CodeGenerator
	 * 
	 * @param patterns Os patterns que serao adicionados
	 */
	protected void addAllPatterns(String[] patterns) {
		this.patterns.addAll(Arrays.asList(patterns));
	}
	
	/** 
	 * @return a lista de patterns do CodeGenerator
	 */
	public List<String> getPatterns() {
		return patterns;
	}

	/**
	 * Adiciona uma lista de required properties na lista de required properties do CodeGenerator
	 * 
	 * @param requiredProperties Os propriedades requeridas
	 */
	protected void addAllRequiredProperties(String[] requiredProperties) {
		this.requiredProperties.addAll(Arrays.asList(requiredProperties));
		
	}

	/**
	 * @return a lista de required properties do CodeGenerator
	 */
	public List<String> getRequiredProperties() {
		return requiredProperties;
	}
	
	/**
	 * Adiciona uma lista de tipos de formularios na lista de tippos de formularios do CodeGenerator
	 * 
	 * @param formTypes Os tipos de formularios
	 */
	public void addAllFormTypes(String[] formTypes) {
		this.formTypes.addAll(Arrays.asList(formTypes));
	}

	/**
	 * @return a lista de tipos de formularios do CodeGenerator
	 */
	public List<String> getFormTypes() {
		return formTypes;
	}
}