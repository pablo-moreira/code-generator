package br.com.atos.gc;

import static br.com.atos.utils.StringUtils.firstToLowerCase;

import java.io.BufferedReader;
import java.io.File;
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

import br.com.atos.gc.componente.Componente;
import br.com.atos.gc.componente.GridXhtmlColunasComponente;
import br.com.atos.gc.componente.GridXhtmlFiltrosComponente;
import br.com.atos.gc.componente.VisualizarXhtmlComponente;
import br.com.atos.gc.componente.WinFrmJavaAtributosComponente;
import br.com.atos.gc.componente.WinFrmJavaImportsComponente;
import br.com.atos.gc.componente.WinFrmJavaMetodosComponente;
import br.com.atos.gc.componente.WinFrmXhtmlAssociacoesComponente;
import br.com.atos.gc.componente.WinFrmXhtmlComponente;
import br.com.atos.gc.modelo.Atributo;
import br.com.atos.gc.modelo.AtributoManyToOne;
import br.com.atos.gc.modelo.Entidade;
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
	public static final String DIR_WEBCONTENT = "dirWebContent";	
	public static final String JAVA = "java";
	public static final String XHTML = "xhtml";
	public static final String ATRIBUTO_ENTIDADE_NOME_UC = "EntidadeNome";
	public static final String ATRIBUTO_ENTIDADE_NOME = "entidadeNome";
	public static final String ATRIBUTO_ENTIDADE_AUDITADA = "entidadeAuditada";
	
	public Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z]*)\\}");	
	private File dirSrc;	
	private File dirWebContent;	
	private HashMap<String, String> atributosValores;
	private List<Componente> componentes;
	private Properties properties;
	private Entidade entidade;
	private List<String> metodoCriadosEmAutoCompleteCtrl = new ArrayList<String>();
	private List<String> metodoCriadosEmSelectItemsCtrl = new ArrayList<String>();
	private List<String> atributosIgnorados = new ArrayList<String>();

	public Entidade getEntidade() {
		return entidade;
	}

	private void inicializarRotuloEhArtigoSeNecessario() {

		if (!getEntidade().isInicializadoRotuloEhArtigo()) {
			
			getEntidade().inicializarRotuloEhArtigoSeNecessario();
			
			atributosValores.put("ArtigoDefinido", getEntidade().getArtigoDefinido().toUpperCase());
			atributosValores.put("artigoDefinido", getEntidade().getArtigoDefinido());		
			atributosValores.put("entidadeRotulo", getEntidade().getRotulo());
			atributosValores.put("EntidadeRotulo", StringUtils.firstToUpperCase(getEntidade().getRotulo()));
		}
	}
	
	private void inicializarEntidadeAtributosSeNecessario() {
		
		if (!getEntidade().isInicializadoAtributos()) {
			getEntidade().inicializarAtributosSeNecessario();
		}
	}
	
	/**
	 * Exemplo de utilizacao do gerador:
	 *	GeradorCodigo gerador = new GeradorCodigo(Produto.class);
	 *	gerador.gerarTudo();
	 */
	public GeradorCodigo(Class<? extends IBaseEntity<?>> entidadeClass) throws Exception {

		entidade = new Entidade(entidadeClass, this);
		
		InputStream inputStream = this.getClass().getResourceAsStream("/gc.properties");

		properties = new Properties();
		properties.load(inputStream);
		
		atributosValores = new HashMap<String,String>();
		
		for (Object key : properties.keySet()) {
			atributosValores.put(key.toString(), properties.getProperty(key.toString()));
		}
		
		atributosValores.put(ATRIBUTO_ENTIDADE_AUDITADA, getEntidade().isAuditada() ? "true" : "false");
		atributosValores.put(ATRIBUTO_ENTIDADE_NOME_UC, getEntidade().getTipoSimpleName());
		atributosValores.put(ATRIBUTO_ENTIDADE_NOME, firstToLowerCase(getEntidade().getTipoSimpleName()));
		atributosValores.put(PACOTE_ENTIDADE, entidadeClass.getPackage().getName());
		
		String value = properties.getProperty("atributosIgnorados");
		
		if (value != null && !value.isEmpty()) {

			StringTokenizer st = new StringTokenizer(value, ",");

			int tokens = st.countTokens();  
			String[] retorno = new String[tokens];  
	  
	        for (int i = 0; i < tokens; i++) {
	        	retorno[i] = st.nextToken();
	        }
	        
	        atributosIgnorados = Arrays.asList(retorno);
		}
		else {
			atributosIgnorados = new ArrayList<String>();
		}
		
		componentes = new ArrayList<Componente>();
		componentes.add(new WinFrmXhtmlComponente(this));
		componentes.add(new WinFrmXhtmlAssociacoesComponente(this));
		componentes.add(new WinFrmJavaAtributosComponente(this));
		componentes.add(new WinFrmJavaMetodosComponente(this));
		componentes.add(new WinFrmJavaImportsComponente(this));
		componentes.add(new GridXhtmlFiltrosComponente(this));
		componentes.add(new GridXhtmlColunasComponente(this));
		componentes.add(new VisualizarXhtmlComponente(this));

		String dirProjeto = System.getProperty("user.dir");
		
		dirSrc = new File(dirProjeto + "/" + properties.getProperty(DIR_SRC));
		dirWebContent = new File(dirProjeto + "/" + properties.getProperty(DIR_WEBCONTENT));

		// Verifica se os diretorios existe se nao existir mostrar msg de erro
		if (!dirSrc.exists() || !dirSrc.isDirectory()) {
			JOptionPane.showMessageDialog(null, MessageFormat.format("O diretório de codigo fonte ({0}) configurado no arquivo gc.properties é inválido!", properties.getProperty(DIR_SRC)), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		if (!dirWebContent.exists() || !dirWebContent.isDirectory()) {
			JOptionPane.showMessageDialog(null, MessageFormat.format("O diretório do conteudo WEB ({0}) configurado no arquivo gc.properties é inválido!", properties.getProperty(DIR_WEBCONTENT)), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		try {
			Field fieldId = JpaReflectionUtils.getFieldId(entidadeClass);			
			atributosValores.put("entidadeIdClass",  fieldId.getType().getSimpleName());			
		}
		catch (Exception e) {
			throw new Exception("Erro ao obter o atributo 'entidadeIdClass' da classe " + getEntidade().getTipoSimpleName());
		}
	}
	
	public void addComponente(Componente novoComponente) {
		
		Componente componente = recuperarComponentePorChave(novoComponente.getComponenteChave());
		
		if (componente != null) {
			componentes.remove(componente);
		}
		
		componentes.add(novoComponente);
	}

	public void gerarDaoEhManager() throws Exception {
		try {
			gerarArquivoPorTipo("DAO", JAVA, false, new File(dirSrc, getAtributoValor(PACOTE_DAO).replace(".", "/")), false, false, false);
			gerarArquivoPorTipo("Manager", JAVA, false, new File(dirSrc, getAtributoValor(PACOTE_MANAGER).replace(".", "/")), false, false, false);
		}
		catch (Exception e) {			
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	public void gerarGrid() throws Exception {
		try {			
			gerarArquivoPorTipo("Grid", XHTML, true, new File(dirWebContent, "resources/components/custom"), true, true, true);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	public void gerarWinFrm() throws Exception {
		try {	
			gerarArquivoPorTipo("WinFrm", JAVA, true, new File(dirSrc, getAtributoValor(PACOTE_WINFRM).replace(".", "/")), true, true, true);
			gerarArquivoPorTipo("WinFrm", XHTML, true, new File(dirWebContent, "resources/components/custom"), true, true, true);
						
			for (Atributo atributo : getEntidade().getAtributos()) {				
				if (atributo instanceof AtributoManyToOne) {
					adicionaMetodoOnCompleteAtributoManytoOneNaClasseAutoCompleteCtrlSeNecessario((AtributoManyToOne) atributo);
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
			gerarArquivoPorTipo("VisualizarCtrl", JAVA, false, new File(dirSrc, getAtributoValor(PACOTE_CONTROLADOR).replace(".", "/")), true, true, false);
			gerarArquivoPorTipo("Visualizar", XHTML, false, new File(dirWebContent, "pages/" + firstToLowerCase(getEntidade().getTipoSimpleName())), true, true, true);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
	
	public void gerarTelaAdministracao() throws Exception {
		try {
			gerarArquivoPorTipo("AdministrarCtrl", JAVA, false, new File(dirSrc, getAtributoValor(PACOTE_CONTROLADOR).replace(".", "/")), false, false, false);
			gerarArquivoPorTipo("Administrar", XHTML, false, new File(dirWebContent, "pages/" + firstToLowerCase(getEntidade().getTipoSimpleName())), true, true, false);		
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
		return atributosValores.get(atributoNome);
	}
	
	private void gerarArquivoPorTipo(String recurso, String tipo, boolean recursoInicio, File dirDestino, boolean permiteSobrescrever, boolean inicializarRotuloEhArtigo, boolean inicializarClasseAtributos) throws Exception {
		
		String arquivoNome;

		if (!dirDestino.exists()) {
			dirDestino.mkdirs();
		}
		
		if (JAVA.equals(tipo)) {
			arquivoNome = (recursoInicio ? recurso + getEntidade().getTipoSimpleName() : getEntidade().getTipoSimpleName() + recurso) + "." + tipo;
		}
		else {
			arquivoNome = (recursoInicio ? recurso + getEntidade().getTipoSimpleName() : getEntidade().getTipoSimpleName() + recurso) + "." + tipo;
			arquivoNome = arquivoNome.substring(0,1).toLowerCase() + arquivoNome.substring(1);
		}

		//File arquivo = new File("/home/pablo-moreira/Desktop/", arquivoNome);
		File arquivo = new File(dirDestino, arquivoNome);
				
		// Verifica se o arquivo existe
		if (arquivo.exists()) {

			if (!permiteSobrescrever) {
				System.out.println(" - " + arquivo.getName() + " [IGNORADO]");				
				return;
			}
			else {
				int resultado = JOptionPane.showConfirmDialog(null, "Você tem certeza que deseja substituir o arquivo: " + arquivo.getName(), "Substituir arquivo",JOptionPane.YES_NO_OPTION);
			
				if (JOptionPane.YES_OPTION != resultado) {
					System.out.println(" - " + arquivo.getName() + " [IGNORADO]");					
					return;
				}			
			}
		}
		
		if (inicializarRotuloEhArtigo) {
			inicializarRotuloEhArtigoSeNecessario();
		}
		
		if (inicializarClasseAtributos) {
			inicializarEntidadeAtributosSeNecessario();
		}
				
		System.out.println(" - " + arquivo.getName() + " [GERADO]");

		String path = "/templates/" + recurso + "." + tipo + ".tpl";
			
		BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(path)));

        PrintWriter pw = new PrintWriter(arquivo);
        
        String linha;

        while ((linha = in.readLine()) != null) {        	        	
        	
        	Matcher matcher = pattern.matcher(linha);
        	
        	String restoLinha = linha;
        	
            while (matcher.find()) {
            	
            	String tplChave = matcher.group();
            	String chave = matcher.group(1);
            	
            	String parteAtualizar = restoLinha.substring(0, restoLinha.indexOf(tplChave) + tplChave.length());
            	restoLinha = restoLinha.substring(restoLinha.indexOf(tplChave) + tplChave.length());
            	
            	if (atributosValores.containsKey(chave)) {	            		
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
		
		for (Componente componente : componentes) {
			if (componente.getComponenteChave().equals(chave)) {
				return componente;
			}
		}
		
		return null;
	}	
	

	private void adicionaMetodoGetEntidadeItensNaClasseSelectItensSeNecessario(Atributo atributo) {
		
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
	
	private void adicionaMetodoOnCompleteAtributoManytoOneNaClasseAutoCompleteCtrlSeNecessario(AtributoManyToOne atributo) {
				
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
					String rotulo = atributo.getAssociacaoAtributoDescricao();					

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
		return atributosIgnorados;
	}
}