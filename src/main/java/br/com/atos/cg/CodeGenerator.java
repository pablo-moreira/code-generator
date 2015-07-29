package br.com.atos.cg;

import static br.com.atos.utils.StringUtils.firstToLowerCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import br.com.atos.cg.component.Component;
import br.com.atos.cg.gui.WinFrmCodeGeneration;
import br.com.atos.cg.model.Attribute;
import br.com.atos.cg.model.AttributeFormType;
import br.com.atos.cg.model.AttributeManyToOne;
import br.com.atos.cg.model.AttributeOneToMany;
import br.com.atos.cg.model.Entity;
import br.com.atos.cg.model.Target;
import br.com.atos.cg.model.TargetConfig;
import br.com.atos.cg.util.LinkedProperties;
import br.com.atos.cg.util.Util;
import br.com.atos.core.model.BaseEnum;
import br.com.atos.core.model.IBaseEntity;
import br.com.atos.core.util.JpaReflectionUtils;
import br.com.atos.utils.DataUtils;
import br.com.atos.utils.OsUtil;

import com.github.cg.model.NewTarget;
import com.github.cg.model.Plugin;
import com.github.cg.model.TargetContext;
import com.github.cg.model.TargetTask;
import com.github.cg.task.Task;
import com.github.cg.task.TaskResult;

public class CodeGenerator {
	
	private static final Logger log = Logger.getLogger(CodeGenerator.class);

	public static final String PACKAGE_BASE = "packageBase";
	public static final String PACKAGE_MODEL = "packageModel";
	public static final String PACKAGE_DAO = "packageDAO";
	public static final String PACKAGE_MANAGER = "packageManager";	
	public static final String PACKAGE_WINFRM = "packageWinFrm";
	public static final String PACKAGE_CONTROLLER = "packageController";	
	public static final String DIRS_SRC = "dirs.src";
	public static final String DIRS_RESOURCES = "dirs.resources";
	public static final String DIRS_WEBCONTENT = "dirs.web";	
	public static final String JAVA = "java";
	public static final String XHTML = "xhtml";
	public static final String ATTRIBUTE_ENTITY_NAME_UC = "EntityName";
	public static final String ATTRIBUTE_ENTITY_NAME = "entityName";
	public static final String ATTRIBUTE_ENTITY_AUDIT = "entityAudit";	
	public static final String CG_PROPERTIES_FILENAME = "cg.properties";
	public static final String MESSAGES_PROPERTIES_FILENAME = "messages.properties";
	
	public static final String PAGE_VIEW_SUFFIX = "page.view.suffix";
	public static final String PAGE_MANAGER_SUFFIX = "page.manager.suffix";
		
	public Pattern pattern = Pattern.compile("\\$\\{([a-z\\.A-Z]*)\\}");	
	private File dirSrc;	
	private File dirWebContent;	
	private HashMap<String,String> attributesValues = new HashMap<String,String>();
	private List<Component> components;	
	private Entity entity;
	private List<String> methodsCreatedsInAutoCompleteCtrl = new ArrayList<String>();
	private List<String> metodoCriadosEmSelectItemsCtrl = new ArrayList<String>();
	private List<String> ignoredAttributes = new ArrayList<String>();
	private LinkedProperties cgProperties;
	private LinkedProperties messagesProperties;
	private File dirBase;
	private File dirResources;
	private Target target;
	private VelocityEngine velocityEngine;
	private HashMap<String, Object> app;
	private List<Plugin> plugins = new ArrayList<Plugin>();
	private TargetContext currentTarget;
	
	public Entity getEntity() {
		return entity;
	}
	
	@SuppressWarnings("unchecked")
	private void createAppItem(String key, String value) {
		
		String[] split = key.split("\\.");
		
		HashMap<String,Object> parent = app;
		
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
	 *	GeradorCodigo gerador = new GeradorCodigo(Produto.class);
	 *	gerador.gerarTudo();
	 */
	public CodeGenerator(Class<? extends IBaseEntity<?>> entidadeClass) throws Exception {
	
		dirBase = new File(System.getProperty("user.dir"));
		
		app = new HashMap<String, Object>();
		
		createAppItem("dirs.base", dirBase.getAbsolutePath());
		
		loadCgProperties();
		
		Iterator<Entry<String, String>> iterator = cgProperties.iterator();
		
		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			createAppItem(entry.getKey(), entry.getValue());
		}		
		
		dirSrc = new File(dirBase, cgProperties.getProperty(DIRS_SRC, ""));
		dirResources = new File(dirBase,  cgProperties.getProperty(DIRS_RESOURCES, ""));
		dirWebContent = new File(dirBase, cgProperties.getProperty(DIRS_WEBCONTENT, ""));
		
		// Verifica se os diretorios existe se nao existir mostrar msg de erro
		if (!dirSrc.exists() || !dirSrc.isDirectory()) {
			JOptionPane.showMessageDialog(null, MessageFormat.format("O diretório de codigo fonte ({0}) configurado no arquivo cg.properties é inválido!", cgProperties.getProperty(DIRS_SRC)), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		if (!dirWebContent.exists() || !dirWebContent.isDirectory()) {
			JOptionPane.showMessageDialog(null, MessageFormat.format("O diretório de recursos ({0}) configurado no arquivo cg.properties é inválido!", cgProperties.getProperty(DIRS_RESOURCES)), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		if (!dirWebContent.exists() || !dirWebContent.isDirectory()) {
			JOptionPane.showMessageDialog(null, MessageFormat.format("O diretório do conteudo WEB ({0}) configurado no arquivo cg.properties é inválido!", cgProperties.getProperty(DIRS_WEBCONTENT)), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		loadMessagesProperties();
		
		loadIgnoredAttributes();
		
		entity = new Entity(entidadeClass, this);
			
		attributesValues.put(PAGE_MANAGER_SUFFIX, "Manager");
		attributesValues.put(PAGE_VIEW_SUFFIX, "View");		
		
//		// Copia do gcProperties
//		Set<String> propertyNames = gcProperties.stringPropertyNames();
//		for (String propertyName : propertyNames) {			
//			attributesValues.put(propertyName, gcProperties.getProperty(propertyName));
//		}
		
		attributesValues.put(ATTRIBUTE_ENTITY_AUDIT, getEntity().isAudited() ? "true" : "false");
		attributesValues.put(ATTRIBUTE_ENTITY_NAME_UC, getEntity().getClassSimpleName());
		attributesValues.put(ATTRIBUTE_ENTITY_NAME, firstToLowerCase(getEntity().getClassSimpleName()));
		attributesValues.put(PACKAGE_MODEL, entidadeClass.getPackage().getName());
				
		components = new ArrayList<Component>();

		try {
			entity.getAttributeId().getType().getSimpleName();			
		}
		catch (Exception e) {
			throw new Exception("Erro ao obter o atributo 'entityIdClass' da classe " + getEntity().getClassSimpleName());
		}

        try {
            if (OsUtil.isOsLinux()) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            }
            else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }  
        }
        catch (Exception e) {}
        
        initVelocityEngine();
	}

	protected String mergeTemplate(VelocityContext context, String templateString) {
		
		try {
			StringWriter writer = new StringWriter();
			
			RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
			StringReader reader = new StringReader(templateString);
			SimpleNode node = runtimeServices.parse(reader, "template");
			
			Template template = new Template();
			template.setRuntimeServices(runtimeServices);
			template.setData(node);
			template.initDocument();					
			template.merge(context, writer);
			
			return writer.toString();
		}
		catch (Exception e) {
			throw new RuntimeException("Erro ao mesclar o template, mensagem interna: " + e.getMessage());
		}
	}
	
	private void initVelocityEngine() {

		velocityEngine = new VelocityEngine();
		
		URL url = CodeGenerator.class.getResource("/com.github.cg.templates");

		File file = new File(url.getFile());

		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
		velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, file.getAbsolutePath());
		velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, "true");						
		velocityEngine.init();
	}

	private void loadIgnoredAttributes() {

		String value = cgProperties.getProperty("ignoredAttributes");
		
		if (value != null && !value.isEmpty()) {

			StringTokenizer st = new StringTokenizer(value, ",");

			int tokens = st.countTokens();  
			
			ignoredAttributes = new ArrayList<String>();  
	  
	        for (int i = 0; i < tokens; i++) {
	        	ignoredAttributes.add(st.nextToken());
	        }

		}
		else {
			ignoredAttributes = new ArrayList<String>();
		}		
	}

	private void loadMessagesProperties() throws Exception {
		
		File messagesFile = getMessagesPropertiesFile();
		
		// Se nao encontrar o arquivo messages.properties cria
		if (messagesFile.exists() == false) {		
			messagesFile.createNewFile();
		}
		
		messagesProperties = new LinkedProperties();
		messagesProperties.load(new FileInputStream(messagesFile));		
	}

	private File getMessagesPropertiesFile() {
		return new File(dirResources, MESSAGES_PROPERTIES_FILENAME);
	}
	
	private File getCgPropertiesFile() {
		return new File(dirResources, CG_PROPERTIES_FILENAME);
	}

	private void loadCgProperties() throws Exception {
		
		// Tenta recuperar o gc.properties pelo classPath
		InputStream is = this.getClass().getResourceAsStream("/" + CG_PROPERTIES_FILENAME);
		
		if (is == null) {
			throw new RuntimeException("O arquivo cg.properties não foi encontrado no classpath!");
		}
		
		loadCgProperties(is);
	}
	
	private void loadCgProperties(InputStream is) throws Exception {
		cgProperties = new LinkedProperties();
		cgProperties.load(is);
	}

	public void addComponent(Component newComponent) {
		
		Component component = recuperarComponentePorChave(newComponent.getComponentKey());
		
		if (component != null) {
			components.remove(component);
		}
		
		newComponent.initialize(this);
		
		components.add(newComponent);
	}

	public void makeDaoAndManager() throws Exception {
		

//		makeTarget(new Target("{0}DAO.java", new File(dirSrc, getAttributeValue(PACKAGE_DAO).replace(".", "/")), "DAO.java.tpl", false));
//		makeTarget(new Target("{0}Manager.java", new File(dirSrc, getAttributeValue(PACKAGE_MANAGER).replace(".", "/")), "Manager.java.tpl", false));
	}
	
	public void makeGrid() throws Exception {
		makeTarget(new Target("Grid{0}.xhtml", new File(dirWebContent, "resources/components/app"), "Grid.xhtml.tpl", true
				, new TargetConfig(true, true, false, true, false, false)
				, null
		));
	}

	public void makeWinFrm() throws Exception {
		makeTarget(new Target("WinFrm{0}.xhtml", new File(dirWebContent, "resources/components/app"), "WinFrm.xhtml.tpl", true					 
				, new TargetConfig(false, false, true, true, true, true)
				, new TargetConfig(true, false, false, true, false, false)
		));
		makeTarget(new Target("WinFrm{0}.java", new File(dirSrc, getAttributeValue(PACKAGE_WINFRM).replace(".", "/")), "WinFrm.java.tpl", true));

		for (Attribute attribute : getEntity().getAttributes()) {				
			
			if (attribute.isRenderForm()) {
			
				if (attribute instanceof AttributeManyToOne) {
					addMethodOnCompleteAttributeManytoOneOnClassAutoCompleteCtrlIfNecessary((AttributeManyToOne) attribute);
				}
				else if (attribute instanceof AttributeOneToMany) {
					
					AttributeOneToMany attrOneToMany = (AttributeOneToMany) attribute;
					
					if (AttributeFormType.INTERNAL.equals(attrOneToMany.getFormType())) {
						for (Attribute assocAttribute : attrOneToMany.getAssociationAttributesWithoutAttributeMappedByAndAttributesOneToMany()) {
							if (assocAttribute.isRenderColumn()) {
								if (assocAttribute instanceof AttributeManyToOne) {
									addMethodOnCompleteAttributeManytoOneOnClassAutoCompleteCtrlIfNecessary((AttributeManyToOne) assocAttribute);
								}
								else if (BaseEnum.class.isAssignableFrom(assocAttribute.getType())) {
									addMethodGetEntityItemsOnClassSelectItemsIfNecessary(assocAttribute);
								}
							}
						}
					}
				}
				else if (BaseEnum.class.isAssignableFrom(attribute.getType())) {
					addMethodGetEntityItemsOnClassSelectItemsIfNecessary(attribute);
				}
			}
		}
	}
		
	private void makeTarget(Target target2) {

		
	}

	public void makePageView() throws Exception {				
		makeTarget(new Target("{0}" + getAttributeValue(PAGE_VIEW_SUFFIX) + "Ctrl.java", new File(dirSrc, getAttributeValue(PACKAGE_CONTROLLER).replace(".", "/")), "ViewCtrl.java.tpl", true));
		makeTarget(new Target("{0}" + getAttributeValue(PAGE_VIEW_SUFFIX) + ".xhtml", new File(dirWebContent, "pages/" + firstToLowerCase(getEntity().getClassSimpleName())), "View.xhtml.tpl", true
				, new TargetConfig(false, false, false, true, false, true)
				, new TargetConfig(true, false, false, true, false, false)
		));		
	}
		
	public void makePageManager() throws Exception {
		makeTarget(new Target("{0}" + getAttributeValue(PAGE_MANAGER_SUFFIX) +  "Ctrl.java", new File(dirSrc, getAttributeValue(PACKAGE_CONTROLLER).replace(".", "/")), "ManagerCtrl.java.tpl", false));
		makeTarget(new Target("{0}" + getAttributeValue(PAGE_MANAGER_SUFFIX) +  ".xhtml", new File(dirWebContent, "pages/" + firstToLowerCase(getEntity().getClassSimpleName())), "Manager.xhtml.tpl", true
				, new TargetConfig(false, false, false, false, false, false)
				, null
		));
	}
	
	public void makeAll() throws Exception {
		makeDaoAndManager();
		makeWinFrm();
		makeGrid();
		makePageView();
		makePageManager();
	}

	public String getAttributeValue(String attributeName) {
		return attributesValues.get(attributeName);
	}
	
	public Target getTarget() {
		return target;
	}
	
	private VelocityContext createContext() {

		VelocityContext context = new VelocityContext();
		
		context.put("util", new Util());
		context.put("entity", entity);
		context.put("app", app);			
		context.put("msgs", messagesProperties);
				
		for (String key : attributesValues.keySet()) {
			context.put(key, attributesValues.get(key));
		}

		for (Component component : getComponents()) {
			context.put(component.getComponentKey(), component);
		}
		
		return context;
	}
	
	private List<Component> getComponents() {

		List<Component> components = new ArrayList<Component>();

		for (Plugin plugin : getPlugins()) {
			components.addAll(plugin.getComponents());
		}

		return components;
	}

	protected void execute(NewTarget target) {
						
		TargetContext targetContext = new TargetContext(target, entity, createContext());
		
		String filename = mergeTemplate(targetContext.getContext(), target.getFilenameTemplate());
		
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
		
		executeTasks(targetContext, target.getTasksToExecuteBefore());
				
		PrintWriter writer;
		
		try {
			writer = new PrintWriter(file);
		}
		catch (Exception e) {
			throw new RuntimeException("Não foi possível encontrar o arquivo, mensagem interna: " + e.getMessage());
		}
		
		Template template = velocityEngine.getTemplate(target.getTemplate());
		template.merge(this.currentTarget.getContext(), writer);

		writer.close();
		
		executeTasks(targetContext, target.getTasksToExecuteAfter());
		
        log.info(file.getName() + " [GERADO]");
	}

	private void executeTasks(TargetContext targetContext, List<TargetTask> targetTasks) {

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
			Class<?> taskClass = Class.forName(targetTask.getTask());	
			Task task = (Task) taskClass.newInstance();
			task.init(this, targetContext, targetTask);
			return task;
		}
		catch (Exception e) {
			log.error("Não foi possível instanciar task!");
		}
		
		return null;
	}

	public void store(Entity entity) {
			
		try {
			String doc = "## Gerado por " + System.getProperty("user.name") + " em: " + DataUtils.getDataHorario(new Date());
			
			getGcProperties().addComment("");
			getGcProperties().addComment(doc);
			
			getMessagesProperties().addComment("");
			getMessagesProperties().addComment(doc);
						
			entity.store();			

			getGcProperties().store(new FileOutputStream(getCgPropertiesFile()), null);
			getMessagesProperties().store(new FileOutputStream(getMessagesPropertiesFile()), null);
			
			loadCgProperties(new FileInputStream(getCgPropertiesFile()));
			loadMessagesProperties();
		}
		catch (Exception e) {
			throw new RuntimeException("Erro ao gravar os arquivos cg.properties e messages.properties, msg interna: " + e.getMessage());
		}
	}

	private Component recuperarComponentePorChave(String chave) {
		
		for (Component componente : components) {
			if (componente.getComponentKey().equals(chave)) {
				return componente;
			}
		}
		
		return null;
	}	
	

	private void addMethodGetEntityItemsOnClassSelectItemsIfNecessary(Attribute atributo) {
		
		try {
			String metodoNome = "get" + atributo.getType().getSimpleName() + "Itens";
		
			// Verifica se o metodo foi criado nesta nessao
			if (metodoCriadosEmSelectItemsCtrl.contains(metodoNome)) {
				return;
			}
			
			Class<?> selectItemsClass = Class.forName(getAttributeValue(PACKAGE_CONTROLLER) + "." + "SelectItems");
	
			// Verifica se o autoCompleteCtrl possui um metodo public List<AssociacaoClasse> onCompleteAssociacaoClasse()
			try {
				// Se achar nao faz nada
				selectItemsClass.getMethod(metodoNome);
			}			
			catch (Exception e) {
				
				// Se nao achar cria o metodo
				try {
					String selectItemsPath = dirSrc.getAbsolutePath() + "/" + selectItemsClass.getName().replace(".", "/") + ".java";
					
					BufferedReader in = new BufferedReader(new FileReader(selectItemsPath));
					
					List<String> arquivoLinhas = new ArrayList<String>();
					
			        String linha;

			        while ((linha = in.readLine()) != null) {
						arquivoLinhas.add(linha);
					}
			        
					in.close();

					// Remove a ultima chave												
					for (int i = arquivoLinhas.size() -1; i >= 0; i--) {
						linha = arquivoLinhas.get(i);
						if (linha.lastIndexOf("}") != -1) {
							linha = linha.substring(0, linha.lastIndexOf("}"));
							arquivoLinhas.set(i, linha);
							break;
						}
					}												
					
					// Procura o index da declaracao do package
					for (int i = 0, t = arquivoLinhas.size(); i < t; i++) {
						linha = arquivoLinhas.get(i);
						if (linha.contains("package")) {
							arquivoLinhas.add(i+1, "");
							arquivoLinhas.add(i+1, MessageFormat.format("import {0};", atributo.getType().getName()));
							break;
						}
					}
					
					arquivoLinhas.add("\t");
					arquivoLinhas.add(MessageFormat.format("\tpublic List<SelectItem> get{0}Itens() '{'", atributo.getType().getSimpleName()));
					arquivoLinhas.add(MessageFormat.format("\t\treturn getEnumSelectItens({0}.class);", atributo.getType().getSimpleName()));
					arquivoLinhas.add("\t}");
					arquivoLinhas.add("}");
					
					PrintWriter pw = new PrintWriter(new File(selectItemsPath));
					
					for (String arquivoLinha : arquivoLinhas) {
						pw.println(arquivoLinha);
					}
					
					pw.close();		
					
					metodoCriadosEmSelectItemsCtrl.add(metodoNome);
				}
				catch (Exception e2) {}	
			}
		}
		catch (Exception e) {
			
		}		
	}
	
	private void addMethodOnCompleteAttributeManytoOneOnClassAutoCompleteCtrlIfNecessary(AttributeManyToOne attribute) {
				
		try {
			String methodName = "onComplete" + attribute.getType().getSimpleName();

			// Verifica se o metodo foi criado nesta sessao
			if (methodsCreatedsInAutoCompleteCtrl.contains(methodName)) {
				return;
			}
			
			Class<?> autoCompleteClass = Class.forName(getAttributeValue(PACKAGE_CONTROLLER) + "." + "AutoCompleteCtrl");

			Class<?> attributeType = attribute.getType();
						
			// Verifica se o autoCompleteCtrl possui um metodo public List<AssociacaoClasse> onCompleteAssociacaoClasse()
			try {
				// Se achar nao faz nada
				autoCompleteClass.getMethod(methodName, String.class);
			}
			catch (Exception e) {

				// Se nao achar cria o metodo
				try {
					String autoCompletePath = dirSrc.getAbsolutePath() + "/" + autoCompleteClass.getName().replace(".", "/") + ".java";
					
					BufferedReader in = new BufferedReader(new FileReader(autoCompletePath));
					
					List<String> fileLines = new ArrayList<String>();
					
			        String line;

			        while ((line = in.readLine()) != null) {
						fileLines.add(line);
					}
			        
					in.close();

					// Remove a ultima chave												
					for (int i = fileLines.size() -1; i >= 0; i--) {
						line = fileLines.get(i);
						if (line.lastIndexOf("}") != -1) {
							line = line.substring(0, line.lastIndexOf("}"));
							fileLines.set(i, line);
							break;
						}
					}												
					
					// Procura o index da declaracao do package
					for (int i = 0, t = fileLines.size(); i < t; i++) {
						line = fileLines.get(i);
						if (line.contains("package")) {
							fileLines.add(i+1, "");
							fileLines.add(i+1, MessageFormat.format("import {0}.{1};", getAttributeValue(PACKAGE_MODEL), attributeType.getSimpleName()));
							fileLines.add(i+1, MessageFormat.format("import {0}.{1}DAO;", getAttributeValue(PACKAGE_DAO), attributeType.getSimpleName()));
							break;
						}
					}
					
					String id = JpaReflectionUtils.getFieldOrPropertyIdName(attributeType);
					String description = attribute.getDescriptionAttributeOfAssociation();					

					fileLines.add("\t");
					fileLines.add(MessageFormat.format("\tpublic List<{0}> onComplete{0}(String sugestao) '{'", attributeType.getSimpleName()));
					fileLines.add(MessageFormat.format("\t\treturn getDAO({0}DAO.class).retrieveLightBySuggestionOrderByLabel(sugestao, \"{1}\", \"{2}\");", attributeType.getSimpleName(), id, description));
					fileLines.add("\t}");
					fileLines.add("}");
					
					PrintWriter pw = new PrintWriter(new File(autoCompletePath));
					
					for (String arquivoLinha : fileLines) {
						pw.println(arquivoLinha);
					}
					
					pw.close();		
					
					methodsCreatedsInAutoCompleteCtrl.add(methodName);
				}
				catch (Exception e2) {}											
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
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
	
	public void make() throws Exception {
		WinFrmCodeGeneration winFrm = new WinFrmCodeGeneration(null, true);
		winFrm.start(this);
		System.exit(0);
	}

	public List<String> getIgnoredAttributes() {
		return ignoredAttributes;
	}

	public LinkedProperties getGcProperties() {
		return cgProperties;
	}

	public LinkedProperties getMessagesProperties() {
		return messagesProperties;
	}
	
	public boolean isIgnoredAttribute(Attribute attribute) {
		for (String attributeIgnored : getIgnoredAttributes()) {
			if (attribute.getName().equals(attributeIgnored)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Plugin> getPlugins() {
		return plugins;
	}

	public void setPlugins(List<Plugin> plugins) {
		this.plugins = plugins;
	}

	protected void addPlugin(Plugin plugin) {
		getPlugins().add(plugin);
	}
}