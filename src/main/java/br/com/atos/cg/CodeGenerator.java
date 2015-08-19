package br.com.atos.cg;

import static br.com.atos.utils.StringUtils.firstToLowerCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import br.com.atos.cg.gui.FrmCodeGeneration;
import br.com.atos.cg.model.TargetConfig;
import br.com.atos.cg.util.LinkedProperties;
import br.com.atos.core.model.BaseEnum;
import br.com.atos.core.util.JpaReflectionUtils;
import br.com.atos.utils.DataUtils;
import br.com.atos.utils.OsUtil;

import com.github.cg.manager.ManagerRepository;
import com.github.cg.model.Attribute;
import com.github.cg.model.AttributeFormType;
import com.github.cg.model.AttributeManyToOne;
import com.github.cg.model.AttributeOneToMany;
import com.github.cg.model.Entity;
import com.github.cg.model.Plugin;
import com.github.cg.model.Target;
import com.github.cg.model.TargetContext;
import com.github.cg.model.TargetGroup;

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
	public static final String CG_PROPERTIES_FILENAME = "cg.properties";
	public static final String MESSAGES_PROPERTIES_FILENAME = "messages.properties";
	
	public static final String PAGE_VIEW_SUFFIX = "page.view.suffix";
	public static final String PAGE_MANAGER_SUFFIX = "page.manager.suffix";

	public static final String APP_NAME = "Code Generator";
	public static final String APP_VERSION = "0.9.0";
	public static final String APP_TITLE = APP_NAME + " - " + APP_VERSION;
		
	public Pattern pattern = Pattern.compile("\\$\\{([a-z\\.A-Z]*)\\}");	
	private File dirSrc;	
	private File dirWebContent;	
	private HashMap<String,String> attributesValues = new HashMap<String,String>();
	
	private Entity entity;
	private List<String> methodsCreatedsInAutoCompleteCtrl = new ArrayList<String>();
	private List<String> metodoCriadosEmSelectItemsCtrl = new ArrayList<String>();
	private LinkedProperties cgProperties;
	private LinkedProperties messagesProperties;
	private File dirBase;
	private File dirResources;
	private br.com.atos.cg.model.OldTarget target;
	private HashMap<String, Object> app;	
	private List<Class<?>> entitiesClass = new ArrayList<Class<?>>();
	private HashMap<String,Object> components = new HashMap<String,Object>();	
	private List<Plugin> plugins = new ArrayList<Plugin>();
	private ManagerRepository managerRepository = new ManagerRepository(this);
	
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
	 *	CodeGenerator cg = new CodeGenerator();
	 *	cg.start();
	 * @throws Exception 
	 */
	public CodeGenerator() throws Exception {
				
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
				
		CodeGeneratorInitializer initializer = new CodeGeneratorInitializer(this);
		initializer.init();
		
        try {
            if (OsUtil.isOsLinux()) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            }
            else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }  
        }
        catch (Exception e) {}
		
        /* TODO - Refatorar adicionar estas informacoes no app */
		attributesValues.put(PAGE_MANAGER_SUFFIX, "Manager");
		attributesValues.put(PAGE_VIEW_SUFFIX, "View");
		
		/* TODO - Refatorar colocar esta validacao no momento que o usuario selecionar esta entidade para gerar o codigo */ 
//		try {
//			entity.getAttributeId().getType().getSimpleName();			
//		}
//		catch (Exception e) {
//			throw new Exception("Erro ao obter o atributo 'entityIdClass' da classe " + getEntity().getClassSimpleName());
//		}
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

	public void makeDaoAndManager() throws Exception {
//		makeTarget(new Target("{0}DAO.java", new File(dirSrc, getAttributeValue(PACKAGE_DAO).replace(".", "/")), "DAO.java.tpl", false));
//		makeTarget(new Target("{0}Manager.java", new File(dirSrc, getAttributeValue(PACKAGE_MANAGER).replace(".", "/")), "Manager.java.tpl", false));
	}
	
	public void makeGrid() throws Exception {
		makeTarget(new br.com.atos.cg.model.OldTarget("Grid{0}.xhtml", new File(dirWebContent, "resources/components/app"), "Grid.xhtml.tpl", true
				, new TargetConfig(true, true, false, true, false, false)
				, null
		));
	}

	public void makeWinFrm() throws Exception {
		makeTarget(new br.com.atos.cg.model.OldTarget("WinFrm{0}.xhtml", new File(dirWebContent, "resources/components/app"), "WinFrm.xhtml.tpl", true					 
				, new TargetConfig(false, false, true, true, true, true)
				, new TargetConfig(true, false, false, true, false, false)
		));
		makeTarget(new br.com.atos.cg.model.OldTarget("WinFrm{0}.java", new File(dirSrc, getAttributeValue(PACKAGE_WINFRM).replace(".", "/")), "WinFrm.java.tpl", true));

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
		
	private void makeTarget(br.com.atos.cg.model.OldTarget target) {}

	public void makePageView() throws Exception {				
		makeTarget(new br.com.atos.cg.model.OldTarget("{0}" + getAttributeValue(PAGE_VIEW_SUFFIX) + "Ctrl.java", new File(dirSrc, getAttributeValue(PACKAGE_CONTROLLER).replace(".", "/")), "ViewCtrl.java.tpl", true));
		makeTarget(new br.com.atos.cg.model.OldTarget("{0}" + getAttributeValue(PAGE_VIEW_SUFFIX) + ".xhtml", new File(dirWebContent, "pages/" + firstToLowerCase(getEntity().getClassSimpleName())), "View.xhtml.tpl", true
				, new TargetConfig(false, false, false, true, false, true)
				, new TargetConfig(true, false, false, true, false, false)
		));		
	}
		
	public void makePageManager() throws Exception {
		makeTarget(new br.com.atos.cg.model.OldTarget("{0}" + getAttributeValue(PAGE_MANAGER_SUFFIX) +  "Ctrl.java", new File(dirSrc, getAttributeValue(PACKAGE_CONTROLLER).replace(".", "/")), "ManagerCtrl.java.tpl", false));
		makeTarget(new br.com.atos.cg.model.OldTarget("{0}" + getAttributeValue(PAGE_MANAGER_SUFFIX) +  ".xhtml", new File(dirWebContent, "pages/" + firstToLowerCase(getEntity().getClassSimpleName())), "Manager.xhtml.tpl", true
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
	
	public br.com.atos.cg.model.OldTarget getTarget() {
		return target;
	}
	
	public HashMap<String,Object> getComponents() {
		return components;
	}

	public void store(Entity entity) {
			
		try {
			String doc = "## Gerado por " + System.getProperty("user.name") + " em: " + DataUtils.getDataHorario(new Date());
			
			getCgProperties().addComment("");
			getCgProperties().addComment(doc);
			
			getMessagesProperties().addComment("");
			getMessagesProperties().addComment(doc);

			getManagerRepository().getEntityManager().storeEntity(entity, getCgProperties(), getMessagesProperties());

			getCgProperties().store(new FileOutputStream(getCgPropertiesFile()), null);
			getMessagesProperties().store(new FileOutputStream(getMessagesPropertiesFile()), null);
			
			loadCgProperties(new FileInputStream(getCgPropertiesFile()));
			loadMessagesProperties();
		}
		catch (Exception e) {
			throw new RuntimeException("Erro ao gravar os arquivos cg.properties e messages.properties, msg interna: " + e.getMessage());
		}
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

	public HashMap<String, String> getAttributesValues() {
		return attributesValues;
	}

	public HashMap<String, Object> getApp() {
		return app;
	}
}