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
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import br.com.atos.gc.component.Componente;
import br.com.atos.gc.component.GridXhtmlColunasComponente;
import br.com.atos.gc.component.GridXhtmlFiltrosComponente;
import br.com.atos.gc.component.VisualizarXhtmlComponente;
import br.com.atos.gc.component.WinFrmJavaAtributosComponente;
import br.com.atos.gc.component.WinFrmJavaImportsComponente;
import br.com.atos.gc.component.WinFrmJavaMetodosComponente;
import br.com.atos.gc.component.WinFrmXhtmlAssociacoesComponente;
import br.com.atos.gc.component.WinFrmXhtmlComponente;
import br.com.atos.gc.gui.WinFrmEntity;
import br.com.atos.gc.model.Attribute;
import br.com.atos.gc.model.AttributeManyToOne;
import br.com.atos.gc.model.Entity;
import br.com.atos.gc.model.Target;
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
	private HashMap<String, String> attributesValues;
	private List<Componente> components;
	private Properties gcProperties;
	private Entity entity;
	private List<String> metodoCriadosEmAutoCompleteCtrl = new ArrayList<String>();
	private List<String> metodoCriadosEmSelectItemsCtrl = new ArrayList<String>();
	private List<String> ignoredAttributes = new ArrayList<String>();
	private Properties messagesProperties;
	private File dirProjeto;
	private File dirResources;
	private Target target;
	
	public Entity getEntity() {
		return entity;
	}

	private void inicializarRotuloEhArtigoSeNecessario() {

		if (!getEntity().isInicializedLabelAndGender()) {
			
                    WinFrmEntity winFrm = new WinFrmEntity(null, true);                        
                    winFrm.initialize(getEntity());			
                    winFrm.start();
						
                    //getEntity().initializeLabelsAndGenderIfNecessarily();
			
                    attributesValues.put("ArtigoDefinido", getEntity().getGender().getArticle().toUpperCase());
                    attributesValues.put("artigoDefinido", getEntity().getGender().getArticle());		
                    attributesValues.put("entidadeRotulo", getEntity().getLabel());
                    attributesValues.put("EntidadeRotulo", StringUtils.firstToUpperCase(getEntity().getLabel()));
		}
	}
	
	private void inicializarEntidadeAtributosSeNecessario() {
		
		if (!getEntity().isInicializedAttributes()) {
			getEntity().inicializarAtributosSeNecessario();
		}
	}
	
	/**
	 * Exemplo de utilizacao do gerador:
	 *	GeradorCodigo gerador = new GeradorCodigo(Produto.class);
	 *	gerador.gerarTudo();
	 */
	public GeradorCodigo(Class<? extends IBaseEntity<?>> entidadeClass) throws Exception {
	
		dirProjeto = new File(System.getProperty("user.dir"));

		carregarGcProperties();
		
		String dr = gcProperties.getProperty(DIR_RESOURCES);

		dirSrc = new File(dirProjeto, gcProperties.getProperty(DIR_SRC));
		dirResources = new File(dirProjeto,  !StringUtils.isNullOrEmpty(dr) ? dr : "src/main/resources");
		dirWebContent = new File(dirProjeto, gcProperties.getProperty(DIR_WEBCONTENT));

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
		
		File messagesFile = getMessagesPropertiesFile();
		
		// Se nao encontrar o arquivo messages.properties cria
		if (messagesFile.exists() == false) {		
			messagesFile.createNewFile();
		}
		
		messagesProperties = new Properties();
		messagesProperties.load(new FileInputStream(messagesFile));
		
		entity = new Entity(entidadeClass, this);
		
		attributesValues = new HashMap<String,String>();
		
		for (Object key : gcProperties.keySet()) {
			attributesValues.put(key.toString(), gcProperties.getProperty(key.toString()));
		}
		
		attributesValues.put(ATRIBUTO_ENTIDADE_AUDITADA, getEntity().isAuditada() ? "true" : "false");
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
		
		components = new ArrayList<Componente>();
		components.add(new WinFrmXhtmlComponente(this));
		components.add(new WinFrmXhtmlAssociacoesComponente(this));
		components.add(new WinFrmJavaAtributosComponente(this));
		components.add(new WinFrmJavaMetodosComponente(this));
		components.add(new WinFrmJavaImportsComponente(this));
		components.add(new GridXhtmlFiltrosComponente(this));
		components.add(new GridXhtmlColunasComponente(this));
		components.add(new VisualizarXhtmlComponente(this));

		try {
			attributesValues.put("entidadeIdClass",  entity.getAtributoId().getField().getType().getSimpleName());			
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
	
	private File getMessagesPropertiesFile() {
		return new File(dirResources, MESSAGES_PROPERTIES_FILENAME);
	}
	
	private File getGcPropertiesFile() {
		return new File(dirResources, GC_PROPERTIES_FILENAME);
	}

	private void carregarGcProperties() throws Exception {
		
		// Tenta recuperar o gc.properties pelo classPath
		InputStream isGc = this.getClass().getResourceAsStream("/" + GC_PROPERTIES_FILENAME);
		
		if (isGc == null) {
			throw new RuntimeException("O arquivo gc.properties não foi encontrado no classpath!");
		}
		
		gcProperties = new Properties();
		gcProperties.load(isGc);
	}

	public void addComponent(Componente newComponent) {
		
		Componente component = recuperarComponentePorChave(newComponent.getComponenteChave());
		
		if (component != null) {
			components.remove(component);
		}
		
		components.add(newComponent);
	}

	public void gerarDaoEhManager() throws Exception {
		try {
			gerarArquivoPorTipo(new Target("DAO", JAVA, false, new File(dirSrc, getAtributoValor(PACOTE_DAO).replace(".", "/")), false, false));
			gerarArquivoPorTipo(new Target("Manager", JAVA, false, new File(dirSrc, getAtributoValor(PACOTE_MANAGER).replace(".", "/")), false, false));
		}
		catch (Exception e) {			
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	public void gerarGrid() throws Exception {
		try {			
			gerarArquivoPorTipo(new Target("Grid", XHTML, true, new File(dirWebContent, "resources/components/custom"), true, true));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	public void gerarWinFrm() throws Exception {
		try {	
			gerarArquivoPorTipo(new Target("WinFrm", JAVA, true, new File(dirSrc, getAtributoValor(PACOTE_WINFRM).replace(".", "/")), true, true));
			gerarArquivoPorTipo(new Target("WinFrm", XHTML, true, new File(dirWebContent, "resources/components/custom"), true, true));
						
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
			gerarArquivoPorTipo(new Target("VisualizarCtrl", JAVA, false, new File(dirSrc, getAtributoValor(PACOTE_CONTROLADOR).replace(".", "/")), true, true));
			gerarArquivoPorTipo(new Target("Visualizar", XHTML, false, new File(dirWebContent, "pages/" + firstToLowerCase(getEntity().getClazzSimpleName())), true, true));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}		
		finalizar();
	}
	
	private void finalizar() throws Exception {
		gcProperties.store(new FileOutputStream(getGcPropertiesFile()), "");
		messagesProperties.store(new FileOutputStream(getMessagesPropertiesFile()), "");
	}
	
	private void gerarTelaAdministracao() throws Exception {
		try {
			gerarArquivoPorTipo(new Target("AdministrarCtrl", JAVA, false, new File(dirSrc, getAtributoValor(PACOTE_CONTROLADOR).replace(".", "/")), false, false));
			gerarArquivoPorTipo(new Target("Administrar", XHTML, false, new File(dirWebContent, "pages/" + firstToLowerCase(getEntity().getClazzSimpleName())), true, true));
			
			finalizar();
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
	
	private void gerarArquivoPorTipo(Target target) throws Exception {

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
				int resultado = JOptionPane.showConfirmDialog(null, "Você tem certeza que deseja substituir o arquivo: " + file.getName(), "Substituir arquivo",JOptionPane.YES_NO_OPTION);
			
				if (JOptionPane.YES_OPTION != resultado) {
					System.out.println(" - " + file.getName() + " [IGNORADO]");					
					return;
				}			
			}
		}
		
		if (getTarget().isInitializeEntity()) {
			inicializarRotuloEhArtigoSeNecessario();
		}
		
//		if (inicializarRotuloEhArtigo) {
//			inicializarRotuloEhArtigoSeNecessario();
//		}
//		
//		if (inicializarClasseAtributos) {
//			inicializarEntidadeAtributosSeNecessario();
//		}
				
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
            		
            		Componente componente = recuperarComponentePorChave(chave);
            		
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

	private Componente recuperarComponentePorChave(String chave) {
		
		for (Componente componente : components) {
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
					String rotulo = atributo.getAssociationAttributeDescription();					

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

	public Properties getGcProperties() {
		return gcProperties;
	}

	public Properties getMessagesProperties() {
		return messagesProperties;
	}
}