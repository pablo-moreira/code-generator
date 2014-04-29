package br.com.atos.gc;

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
import java.util.Arrays;
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

import br.com.atos.gc.component.Component;
import br.com.atos.gc.component.GridXhtmlColumnsComponent;
import br.com.atos.gc.component.GridXhtmlFilterComponent;
import br.com.atos.gc.component.ViewXhtmlComponent;
import br.com.atos.gc.component.WinFrmJavaAttributesComponent;
import br.com.atos.gc.component.WinFrmJavaImportsComponent;
import br.com.atos.gc.component.WinFrmJavaMethodsComponent;
import br.com.atos.gc.component.WinFrmXhtmlAssociationsComponent;
import br.com.atos.gc.component.WinFrmXhtmlComponent;
import br.com.atos.gc.gui.WinFrmAttributeOneToMany;
import br.com.atos.gc.gui.WinFrmEntity;
import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.model.AttributeManyToOne;
import br.com.atos.gc.model.AttributeOneToMany;
import br.com.atos.gc.model.Entity;
import br.com.atos.gc.model.Target;
import br.com.atos.gc.model.TargetColumnRender;
import br.com.atos.gc.util.LinkedProperties;
import br.com.atos.utils.DataUtils;
import br.com.atos.utils.OsUtil;
import br.com.atos.utils.StringUtils;
import br.com.atosdamidia.comuns.modelo.BaseEnum;
import br.com.atosdamidia.comuns.modelo.IBaseEntity;
import br.com.atosdamidia.comuns.util.JpaReflectionUtils;

public class GeradorCodigo {

	public static final String PACOTE_BASE = "pacoteBase";
	public static final String PACOTE_ENTIDADE = "pacoteEntidade";
	public static final String PACOTE_DAO = "pacoteDAO";
	public static final String PACOTE_MANAGER = "pacoteManager";
	public static final String PACOTE_GRID = "pacoteGrid";
	public static final String PACOTE_WINGRID = "pacoteWinGrid";
	public static final String PACOTE_WINFRM = "pacoteWinFrm";
	public static final String PACOTE_CONTROLADOR = "pacoteControlador";	
	public static final String DIR_SRC = "dirSrc";
	public static final String DIR_RESOURCES = "dirResources";
	public static final String DIR_WEBCONTENT = "dirWebContent";	
	public static final String JAVA = "java";
	public static final String XHTML = "xhtml";
	public static final String ATRIBUTO_ENTIDADE_NOME_UC = "EntidadeNome";
	public static final String ATRIBUTO_ENTIDADE_NOME = "entidadeNome";
	public static final String ATRIBUTO_ENTIDADE_AUDITADA = "entidadeAuditada";	
	public static final String GC_PROPERTIES_FILENAME = "gc.properties";
	public static final String MESSAGES_PROPERTIES_FILENAME = "messages.properties";
		
	public Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z]*)\\}");	
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
	public GeradorCodigo(Class<? extends IBaseEntity<?>> entidadeClass) throws Exception {
	
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
		
		entity = new Entity(entidadeClass, this);
					
		Iterator<Entry<String, String>> iterator = gcProperties.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> key = iterator.next();
			attributesValues.put(key.getKey(), key.getValue());
		}
		
		attributesValues.put(ATRIBUTO_ENTIDADE_AUDITADA, getEntity().isAudited() ? "true" : "false");
		attributesValues.put(ATRIBUTO_ENTIDADE_NOME_UC, getEntity().getClazzSimpleName());
		attributesValues.put(ATRIBUTO_ENTIDADE_NOME, firstToLowerCase(getEntity().getClazzSimpleName()));
		attributesValues.put(PACOTE_ENTIDADE, entidadeClass.getPackage().getName());
		
		String value = gcProperties.getProperty("atributosIgnorados");
		
		if (value != null && !value.isEmpty()) {

			StringTokenizer st = new StringTokenizer(value, ",");

			int tokens = st.countTokens();  
			String[] result = new String[tokens];  
	  
	        for (int i = 0; i < tokens; i++) {
	        	result[i] = st.nextToken();
	        }
	        
	        ignoredAttributes = Arrays.asList(result);
		}
		else {
			ignoredAttributes = new ArrayList<String>();
		}
		
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
			attributesValues.put("entidadeIdClass",  entity.getAttributeId().getField().getType().getSimpleName());			
		}
		catch (Exception e) {
			throw new Exception("Erro ao obter o atributo 'entidadeIdClass' da classe " + getEntity().getClazzSimpleName());
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
		
		Component component = recuperarComponentePorChave(newComponent.getComponenteChave());
		
		if (component != null) {
			components.remove(component);
		}
		
		components.add(newComponent);
	}

	public void gerarDaoEhManager() throws Exception {
		try {
			makeTarget(new Target("DAO", JAVA, false, new File(dirSrc, getAtributoValor(PACOTE_DAO).replace(".", "/")), false, false));
			makeTarget(new Target("Manager", JAVA, false, new File(dirSrc, getAtributoValor(PACOTE_MANAGER).replace(".", "/")), false, false));
		}
		catch (Exception e) {			
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	public void gerarGrid() throws Exception {
		try {
			makeTarget(new Target("Grid", XHTML, true, new File(dirWebContent, "resources/components/custom"), true, true
					, new TargetColumnRender(true, true, false, true, false)
					, new TargetColumnRender(false, false, false, false, false)
			));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	public void gerarWinFrm() throws Exception {
		try {
			makeTarget(new Target("WinFrm", JAVA, true, new File(dirSrc, getAtributoValor(PACOTE_WINFRM).replace(".", "/")), true, true));
			makeTarget(new Target("WinFrm", XHTML, true, new File(dirWebContent, "resources/components/custom"), true, true
					, new TargetColumnRender(false, false, true, true, true)
					, new TargetColumnRender(true, false, true, true, true)
			));
						
			for (Attribute atributo : getEntity().getAttributes()) {				
				if (atributo instanceof AttributeManyToOne) {
					adicionaMetodoOnCompleteAtributoManytoOneNaClasseAutoCompleteCtrlSeNecessario((AttributeManyToOne) atributo);
				}
				else if (BaseEnum.class.isAssignableFrom(atributo.getField().getType())) {
					adicionaMetodoGetEntidadeItensNaClasseSelectItensSeNecessario(atributo);
				}
			}
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	public void gerarTelaVisualizacao() throws Exception {
		try {
			makeTarget(new Target("VisualizarCtrl", JAVA, false, new File(dirSrc, getAtributoValor(PACOTE_CONTROLADOR).replace(".", "/")), true, false));
			makeTarget(new Target("Visualizar", XHTML, false, new File(dirWebContent, "pages/" + firstToLowerCase(getEntity().getClazzSimpleName())), true, true
					, new TargetColumnRender(true, false, false, true, false)
					, new TargetColumnRender(true, false, false, true, false)
			));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
		
	private void gerarTelaAdministracao() throws Exception {
		try {
			makeTarget(new Target("AdministrarCtrl", JAVA, false, new File(dirSrc, getAtributoValor(PACOTE_CONTROLADOR).replace(".", "/")), false, false));
			makeTarget(new Target("Administrar", XHTML, false, new File(dirWebContent, "pages/" + firstToLowerCase(getEntity().getClazzSimpleName())), true, true));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	public void gerarTudo() throws Exception {
		gerarDaoEhManager();
		gerarWinFrm();
		gerarGrid();
		gerarTelaAdministracao();
		gerarTelaVisualizacao();
	}

	public String getAtributoValor(String atributoNome) {
		return attributesValues.get(atributoNome);
	}
	
	public Target getTarget() {
		return target;
	}	
	
	private void makeTarget(Target target) throws Exception {

		this.target = target;
		
		String fileName;

		if (!getTarget().getDestinationDirectory().exists()) {
			getTarget().getDestinationDirectory().mkdirs();
		}
		
		if (JAVA.equals(getTarget().getType())) {
			fileName = (getTarget().isResourceStart() ? getTarget().getResource() + getEntity().getClazzSimpleName() : getEntity().getClazzSimpleName() + getTarget().getResource()) + "." + getTarget().getType();
		}
		else {
			fileName = (getTarget().isResourceStart() ? getTarget().getResource() + getEntity().getClazzSimpleName() : getEntity().getClazzSimpleName() + getTarget().getResource()) + "." + getTarget().getType();
			fileName = fileName.substring(0,1).toLowerCase() + fileName.substring(1);
		}

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
		
		if (getTarget().isInitializeEntity()) {

            WinFrmEntity winFrm = new WinFrmEntity(null, true);                        
            winFrm.start(getEntity(), getTarget());
            
            if (!winFrm.isStatusOK()) {
            	System.exit(0);
            }
            else {	
            	store(getEntity());
            }

            attributesValues.put("ArtigoDefinido", getEntity().getGender().getArticle().toUpperCase());
            attributesValues.put("artigoDefinido", getEntity().getGender().getArticle());
            attributesValues.put("entidadeRotulo", getEntity().getLabel());
            attributesValues.put("EntidadeRotulo", StringUtils.firstToUpperCase(getEntity().getLabel()));
                                    
            for (AttributeOneToMany attribute : getEntity().getAttributesOneToMany()) {

            	attribute.initializeAssociationEntity();
            	
            	WinFrmAttributeOneToMany winFrmAttributeOneToMany = new WinFrmAttributeOneToMany(null, true);                                        
            	winFrmAttributeOneToMany.start(attribute, getTarget());

                if (!winFrmAttributeOneToMany.isStatusOK()) {
                	System.exit(0);
                }
                else {
                	store(attribute.getAssociationEntity());
                }
            }
		}
						
		System.out.println(" - " + file.getName() + " [GERADO]");

		String path = "/templates/" + target.getResource() + "." + target.getType() + ".tpl";
			
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
            		pw.print(parteAtualizar.replace(tplChave, getAtributoValor(chave)));
            	}
            	else {
            		
            		Component componente = recuperarComponentePorChave(chave);
            		
            		if (componente != null) {
            			componente.renderizar(pw);
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
			if (componente.getComponenteChave().equals(chave)) {
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
			
			Class<?> selectItemsClass = Class.forName(getAtributoValor(PACOTE_CONTROLADOR) + "." + "SelectItems");
	
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
					
					// Procura o index da declaracao do pacote
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
			
			Class<?> autoCompleteClass = Class.forName(getAtributoValor(PACOTE_CONTROLADOR) + "." + "AutoCompleteCtrl");

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
					
					// Procura o index da declaracao do pacote
					for (int i = 0, t = arquivoLinhas.size(); i < t; i++) {
						linha = arquivoLinhas.get(i);
						if (linha.contains("package")) {
							arquivoLinhas.add(i+1, "");
							arquivoLinhas.add(i+1, MessageFormat.format("import {0}.{1};", getAtributoValor(PACOTE_ENTIDADE), field.getType().getSimpleName()));
							arquivoLinhas.add(i+1, MessageFormat.format("import {0}.{1}DAO;", getAtributoValor(PACOTE_DAO), field.getType().getSimpleName()));
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

	public void gerar() throws Exception {
		
		if (JOptionPane.showConfirmDialog(null, "Gerar tudo, WinFrm, Grid, TelaAdministracao, Dao e Manager?", "Executar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			gerarTudo();
		}
		else {
			if (JOptionPane.showConfirmDialog(null, "Gerar dao e manager?", "Executar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {		
				gerarDaoEhManager();
			}
			
			if (JOptionPane.showConfirmDialog(null, "Gerar WinFrm?", "Executar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				gerarWinFrm();
			}
			
			if (JOptionPane.showConfirmDialog(null, "Gerar Grid?", "Executar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				gerarGrid();
			}
			
			if (JOptionPane.showConfirmDialog(null, "Gerar TelaAdministração?", "Executar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				gerarTelaAdministracao();
			}
			
			if (JOptionPane.showConfirmDialog(null, "Gerar TelaVisualização?", "Executar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				gerarTelaVisualizacao();
			}
		}
	}

	public List<String> getAtributosIgnorados() {
		return ignoredAttributes;
	}

	public LinkedProperties getGcProperties() {
		return gcProperties;
	}

	public LinkedProperties getMessagesProperties() {
		return messagesProperties;
	}
}