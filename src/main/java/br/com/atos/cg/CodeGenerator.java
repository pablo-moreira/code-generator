package br.com.atos.cg;

import static br.com.atos.utils.StringUtils.firstToLowerCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import br.com.atos.cg.component.Component;
import br.com.atos.cg.component.GridXhtmlColumnsComponent;
import br.com.atos.cg.component.GridXhtmlFilterComponent;
import br.com.atos.cg.component.ViewXhtmlComponent;
import br.com.atos.cg.component.WinFrmJavaAttributesComponent;
import br.com.atos.cg.component.WinFrmJavaImportsComponent;
import br.com.atos.cg.component.WinFrmJavaMethodsComponent;
import br.com.atos.cg.component.WinFrmXhtmlAssociationsComponent;
import br.com.atos.cg.component.WinFrmXhtmlComponent;
import br.com.atos.cg.gui.WinFrmAttributeOneToMany;
import br.com.atos.cg.gui.WinFrmCodeGeneration;
import br.com.atos.cg.gui.WinFrmEntity;
import br.com.atos.cg.model.Attribute;
import br.com.atos.cg.model.AttributeFormType;
import br.com.atos.cg.model.AttributeManyToOne;
import br.com.atos.cg.model.AttributeOneToMany;
import br.com.atos.cg.model.Entity;
import br.com.atos.cg.model.Target;
import br.com.atos.cg.model.TargetConfig;
import br.com.atos.cg.util.LinkedProperties;
import br.com.atos.core.model.BaseEnum;
import br.com.atos.core.model.IBaseEntity;
import br.com.atos.core.util.JpaReflectionUtils;
import br.com.atos.utils.DataUtils;
import br.com.atos.utils.OsUtil;
import br.com.atos.utils.StringUtils;

public class CodeGenerator {

	public static final String PACKAGE_BASE = "packageBase";
	public static final String PACKAGE_MODEL = "packageModel";
	public static final String PACKAGE_DAO = "packageDAO";
	public static final String PACKAGE_MANAGER = "packageManager";	
	public static final String PACKAGE_WINFRM = "packageWinFrm";
	public static final String PACKAGE_CONTROLLER = "packageController";	
	public static final String DIR_SRC = "dirSrc";
	public static final String DIR_RESOURCES = "dirResources";
	public static final String DIR_WEBCONTENT = "dirWebContent";	
	public static final String JAVA = "java";
	public static final String XHTML = "xhtml";
	public static final String ATTRIBUTE_ENTITY_NAME_UC = "EntityName";
	public static final String ATTRIBUTE_ENTITY_NAME = "entityName";
	public static final String ATTRIBUTE_ENTITY_AUDIT = "entityAudit";	
	public static final String GC_PROPERTIES_FILENAME = "cg.properties";
	public static final String MESSAGES_PROPERTIES_FILENAME = "messages.properties";
	
	public static final String PAGE_VIEW_SUFFIX = "page.view.suffix";
	public static final String PAGE_MANAGER_SUFFIX = "page.manager.suffix";
		
	public Pattern pattern = Pattern.compile("\\$\\{([a-z\\.A-Z]*)\\}");	
	private File dirSrc;	
	private File dirWebContent;	
	private HashMap<String,String> attributesValues = new HashMap<String,String>();
	private List<Component> components;	
	private Entity entity;
	private List<String> metodoCriadosEmAutoCompleteCtrl = new ArrayList<String>();
	private List<String> metodoCriadosEmSelectItemsCtrl = new ArrayList<String>();
	private List<String> ignoredAttributes = new ArrayList<String>();
	private LinkedProperties gcProperties;
	private LinkedProperties messagesProperties;
	private File dirProject;
	private File dirResources;
	private Target target;
	
	public Entity getEntity() {
		return entity;
	}
		
	/**
	 * Exemplo de utilizacao do gerador:
	 *	GeradorCodigo gerador = new GeradorCodigo(Produto.class);
	 *	gerador.gerarTudo();
	 */
	public CodeGenerator(Class<? extends IBaseEntity<?>> entidadeClass) throws Exception {
	
		dirProject = new File(System.getProperty("user.dir"));

		loadGcProperties();
		
		dirSrc = new File(dirProject, gcProperties.getProperty(DIR_SRC));
		dirResources = new File(dirProject,  gcProperties.getProperty(DIR_RESOURCES, "src/main/resources"));
		dirWebContent = new File(dirProject, gcProperties.getProperty(DIR_WEBCONTENT));

		// Verifica se os diretorios existe se nao existir mostrar msg de erro
		if (!dirSrc.exists() || !dirSrc.isDirectory()) {
			JOptionPane.showMessageDialog(null, MessageFormat.format("O diretório de codigo fonte ({0}) configurado no arquivo gc.properties é inválido!", gcProperties.getProperty(DIR_SRC)), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		if (!dirWebContent.exists() || !dirWebContent.isDirectory()) {
			JOptionPane.showMessageDialog(null, MessageFormat.format("O diretório de recursos ({0}) configurado no arquivo gc.properties é inválido!", gcProperties.getProperty(DIR_RESOURCES)), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		if (!dirWebContent.exists() || !dirWebContent.isDirectory()) {
			JOptionPane.showMessageDialog(null, MessageFormat.format("O diretório do conteudo WEB ({0}) configurado no arquivo gc.properties é inválido!", gcProperties.getProperty(DIR_WEBCONTENT)), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		loadMessagesProperties();
		
		loadIgnoredAttributes();
		
		entity = new Entity(entidadeClass, this);
			
		attributesValues.put(PAGE_MANAGER_SUFFIX, "Manager");
		attributesValues.put(PAGE_VIEW_SUFFIX, "View");		
		
		// Copia do gcProperties
		Iterator<Entry<String, String>> iterator = gcProperties.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> key = iterator.next();
			attributesValues.put(key.getKey(), key.getValue());
		}
		
		attributesValues.put(ATTRIBUTE_ENTITY_AUDIT, getEntity().isAudited() ? "true" : "false");
		attributesValues.put(ATTRIBUTE_ENTITY_NAME_UC, getEntity().getClazzSimpleName());
		attributesValues.put(ATTRIBUTE_ENTITY_NAME, firstToLowerCase(getEntity().getClazzSimpleName()));
		attributesValues.put(PACKAGE_MODEL, entidadeClass.getPackage().getName());
				
		components = new ArrayList<Component>();
		components.add(new WinFrmXhtmlComponent(this));
		components.add(new WinFrmXhtmlAssociationsComponent(this));
		components.add(new WinFrmJavaAttributesComponent(this));
		components.add(new WinFrmJavaMethodsComponent(this));
		components.add(new WinFrmJavaImportsComponent(this));
		components.add(new GridXhtmlFilterComponent(this));
		components.add(new GridXhtmlColumnsComponent(this));
		components.add(new ViewXhtmlComponent(this));

		try {
			attributesValues.put("entityIdClass",  entity.getAttributeId().getField().getType().getSimpleName());			
		}
		catch (Exception e) {
			throw new Exception("Erro ao obter o atributo 'entityIdClass' da classe " + getEntity().getClazzSimpleName());
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
	}
	
	private void loadIgnoredAttributes() {

		String value = gcProperties.getProperty("ignoredAttributes");
		
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
	
	private File getGcPropertiesFile() {
		return new File(dirResources, GC_PROPERTIES_FILENAME);
	}

	private void loadGcProperties() throws Exception {
		
		// Tenta recuperar o gc.properties pelo classPath
		InputStream is = this.getClass().getResourceAsStream("/" + GC_PROPERTIES_FILENAME);
		
		if (is == null) {
			throw new RuntimeException("O arquivo gc.properties não foi encontrado no classpath!");
		}
		
		loadGcProperties(is);
	}
	
	private void loadGcProperties(InputStream is) throws Exception {
		gcProperties = new LinkedProperties();
		gcProperties.load(is);		
	}

	public void addComponent(Component newComponent) {
		
		Component component = recuperarComponentePorChave(newComponent.getComponentKey());
		
		if (component != null) {
			components.remove(component);
		}
		
		components.add(newComponent);
	}

	public void makeDaoAndManager() throws Exception {
		makeTarget(new Target("{0}DAO.java", new File(dirSrc, getAttributeValue(PACKAGE_DAO).replace(".", "/")), "DAO.java.tpl", false));
		makeTarget(new Target("{0}Manager.java", new File(dirSrc, getAttributeValue(PACKAGE_MANAGER).replace(".", "/")), "Manager.java.tpl", false));
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
					adicionaMetodoOnCompleteAtributoManytoOneNaClasseAutoCompleteCtrlSeNecessario((AttributeManyToOne) attribute);
				}
				else if (attribute instanceof AttributeOneToMany) {
					
					AttributeOneToMany attrOneToMany = (AttributeOneToMany) attribute;
					
					if (AttributeFormType.INTERNAL.equals(attrOneToMany.getFormType())) {
						for (Attribute assocAttribute : attrOneToMany.getAssociationAttributesWithoutAttributeMappedByAndAttributesOneToMany()) {
							if (assocAttribute.isRenderColumn()) {
								if (assocAttribute instanceof AttributeManyToOne) {
									adicionaMetodoOnCompleteAtributoManytoOneNaClasseAutoCompleteCtrlSeNecessario((AttributeManyToOne) assocAttribute);
								}
								else if (BaseEnum.class.isAssignableFrom(assocAttribute.getField().getType())) {
									adicionaMetodoGetEntidadeItensNaClasseSelectItensSeNecessario(assocAttribute);
								}
							}
						}
					}
				}
				else if (BaseEnum.class.isAssignableFrom(attribute.getField().getType())) {
					adicionaMetodoGetEntidadeItensNaClasseSelectItensSeNecessario(attribute);
				}				
			}
		}
	}
		
	public void makePageView() throws Exception {				
		makeTarget(new Target("{0}" + getAttributeValue(PAGE_VIEW_SUFFIX) + "Ctrl.java", new File(dirSrc, getAttributeValue(PACKAGE_CONTROLLER).replace(".", "/")), "ViewCtrl.java.tpl", true));
		makeTarget(new Target("{0}" + getAttributeValue(PAGE_VIEW_SUFFIX) + ".xhtml", new File(dirWebContent, "pages/" + firstToLowerCase(getEntity().getClazzSimpleName())), "View.xhtml.tpl", true
				, new TargetConfig(false, false, false, true, false, true)
				, new TargetConfig(true, false, false, true, false, false)
		));		
	}
		
	public void makePageManager() throws Exception {
		makeTarget(new Target("{0}" + getAttributeValue(PAGE_MANAGER_SUFFIX) +  "Ctrl.java", new File(dirSrc, getAttributeValue(PACKAGE_CONTROLLER).replace(".", "/")), "ManagerCtrl.java.tpl", false));
		makeTarget(new Target("{0}" + getAttributeValue(PAGE_MANAGER_SUFFIX) +  ".xhtml", new File(dirWebContent, "pages/" + firstToLowerCase(getEntity().getClazzSimpleName())), "Manager.xhtml.tpl", true
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

	private void makeTarget(Target target) throws Exception {

		this.target = target;
		
		if (!getTarget().getDestinationDirectory().exists()) {
			getTarget().getDestinationDirectory().mkdirs();
		}
		
		String fileName = getTarget().getFileName(getEntity());
		
		File file = new File(getTarget().getDestinationDirectory(), fileName);

		// Verifica se o arquivo existe
		if (file.exists()) {

			if (!getTarget().isAllowOverwrite()) {
				System.out.println(" - " + file.getName() + " [IGNORADO]");				
				return;
			}
			else {
				int result = JOptionPane.showConfirmDialog(null, "Você tem certeza que deseja substituir o arquivo: " + file.getName(), "Substituir arquivo",JOptionPane.YES_NO_OPTION);
			
				if (JOptionPane.YES_OPTION != result) {
					System.out.println(" - " + file.getName() + " [IGNORADO]");					
					return;
				}			
			}
		}
		
		if (getTarget().isShowWinFrmEntity()) {

            WinFrmEntity winFrm = new WinFrmEntity(null, true);                        
            winFrm.start(getEntity(), getTarget());
            
            if (!winFrm.isStatusOK()) {
            	return;
            }
            else {
            	store(getEntity());
            }

            attributesValues.put("Gender", getEntity().getGender().getArticle().toUpperCase());
            attributesValues.put("gender", getEntity().getGender().getArticle());
            attributesValues.put("entityLabel", getEntity().getLabel());
            attributesValues.put("EntityLabel", StringUtils.firstToUpperCase(getEntity().getLabel()));
                             
            if (getTarget().isShowWinFrmAttributeOneToMany()) { 
	            
            	for (AttributeOneToMany attribute : getEntity().getAttributesOneToMany()) {
	
	            	attribute.initializeAssociationEntity();
	            	
	            	WinFrmAttributeOneToMany winFrmAttributeOneToMany = new WinFrmAttributeOneToMany(null, true);                                        
	            	winFrmAttributeOneToMany.start(attribute, getTarget());
	
	                if (!winFrmAttributeOneToMany.isStatusOK()) {
	                	return;
	                }
	                else {
	                	store(attribute.getAssociationEntity());
	                }
	            }
            }
		}
						
		System.out.println(" - " + file.getName() + " [GERADO]");

		String path = "/templates/" + target.getTemplateFileName();
			
		BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));

        PrintWriter pw = new PrintWriter(file);
        
        String linha;

        while ((linha = in.readLine()) != null) {        	        	
        	
        	Matcher matcher = pattern.matcher(linha);
        	
        	String restoLinha = linha;
        	
            while (matcher.find()) {
            	
            	String tplChave = matcher.group();
            	String chave = matcher.group(1);
            	
            	String parteAtualizar = restoLinha.substring(0, restoLinha.indexOf(tplChave) + tplChave.length());
            	restoLinha = restoLinha.substring(restoLinha.indexOf(tplChave) + tplChave.length());
            	
            	if (attributesValues.containsKey(chave)) {	            		
            		pw.print(parteAtualizar.replace(tplChave, getAttributeValue(chave)));
            	}
            	else {
            		
            		Component componente = recuperarComponentePorChave(chave);
            		
            		if (componente != null) {
            			componente.render(pw);
            		}
            		else {
            			pw.print(parteAtualizar);
            		}
            	}
            }
            
           	pw.println(restoLinha);	           
        }
        
        in.close();
        
        pw.close();
	}

	private void store(Entity entity) {
			
		try {
			String doc = "## Gerado por " + System.getProperty("user.name") + " em: " + DataUtils.getDataHorario(new Date());
			
			getGcProperties().addComment("");
			getGcProperties().addComment(doc);
			
			getMessagesProperties().addComment("");
			getMessagesProperties().addComment(doc);
						
			entity.store();			

			getGcProperties().store(new FileOutputStream(getGcPropertiesFile()), null);
			getMessagesProperties().store(new FileOutputStream(getMessagesPropertiesFile()), null);
			
			loadGcProperties(new FileInputStream(getGcPropertiesFile()));
			loadMessagesProperties();
		}
		catch (Exception e) {
			throw new RuntimeException("Erro ao gravar os arquivos gc.properties e messages.properties, msg interna: " + e.getMessage());
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
	

	private void adicionaMetodoGetEntidadeItensNaClasseSelectItensSeNecessario(Attribute atributo) {
		
		try {
			String metodoNome = "get" + atributo.getField().getType().getSimpleName() + "Itens";
		
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
							arquivoLinhas.add(i+1, MessageFormat.format("import {0};", atributo.getField().getType().getName()));
							break;
						}
					}
					
					arquivoLinhas.add("\t");
					arquivoLinhas.add(MessageFormat.format("\tpublic List<SelectItem> get{0}Itens() '{'", atributo.getField().getType().getSimpleName()));
					arquivoLinhas.add(MessageFormat.format("\t\treturn getEnumSelectItens({0}.class);", atributo.getField().getType().getSimpleName()));
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
	
	private void adicionaMetodoOnCompleteAtributoManytoOneNaClasseAutoCompleteCtrlSeNecessario(AttributeManyToOne atributo) {
				
		try {
			String metodoNome = "onComplete" + atributo.getField().getType().getSimpleName();

			// Verifica se o metodo foi criado nesta sessao
			if (metodoCriadosEmAutoCompleteCtrl.contains(metodoNome)) {
				return;
			}
			
			Class<?> autoCompleteClass = Class.forName(getAttributeValue(PACKAGE_CONTROLLER) + "." + "AutoCompleteCtrl");

			Field field = atributo.getField();
						
			// Verifica se o autoCompleteCtrl possui um metodo public List<AssociacaoClasse> onCompleteAssociacaoClasse()
			try {
				// Se achar nao faz nada
				autoCompleteClass.getMethod(metodoNome, String.class);									
			}
			catch (Exception e) {

				// Se nao achar cria o metodo
				try {
					String autoCompletePath = dirSrc.getAbsolutePath() + "/" + autoCompleteClass.getName().replace(".", "/") + ".java";
					
					BufferedReader in = new BufferedReader(new FileReader(autoCompletePath));
					
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
							arquivoLinhas.add(i+1, MessageFormat.format("import {0}.{1};", getAttributeValue(PACKAGE_MODEL), field.getType().getSimpleName()));
							arquivoLinhas.add(i+1, MessageFormat.format("import {0}.{1}DAO;", getAttributeValue(PACKAGE_DAO), field.getType().getSimpleName()));
							break;
						}
					}
					
					String id = JpaReflectionUtils.getFieldId(field.getType()).getName();
					String rotulo = atributo.getDescriptionAttributeOfAssociation();					

					arquivoLinhas.add("\t");
					arquivoLinhas.add(MessageFormat.format("\tpublic List<{0}> onComplete{0}(String sugestao) '{'", field.getType().getSimpleName()));
					arquivoLinhas.add(MessageFormat.format("\t\treturn getDAO({0}DAO.class).recuperarResumoPorSugestaoOrdenadasPorRotulo(sugestao, \"{1}\", \"{2}\");", field.getType().getSimpleName(), id, rotulo));
					arquivoLinhas.add("\t}");
					arquivoLinhas.add("}");
					
					PrintWriter pw = new PrintWriter(new File(autoCompletePath));
					
					for (String arquivoLinha : arquivoLinhas) {
						pw.println(arquivoLinha);
					}
					
					pw.close();		
					
					metodoCriadosEmAutoCompleteCtrl.add(metodoNome);
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
		return gcProperties;
	}

	public LinkedProperties getMessagesProperties() {
		return messagesProperties;
	}
	
	public boolean isIgnoredAttribute(Attribute attribute) {
		for (String attributeIgnored : getIgnoredAttributes()) {
			if (attribute.getField().getName().equals(attributeIgnored)) {
				return true;
			}
		}
		return false;
	}
}