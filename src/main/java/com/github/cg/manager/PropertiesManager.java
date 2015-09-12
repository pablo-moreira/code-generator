package com.github.cg.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.MessageFormat;

import com.github.cg.util.LinkedProperties;

abstract public class PropertiesManager extends BaseManager {

	public PropertiesManager(ManagerRepository repository) {
		super(repository);
	}

	/**
	 * Metodo responsavel por gravar um arquivo de propriedades 
	 * 
	 * @param properties As propriedades que serao gravadas
	 * @param dir O diretorio onde o arquivo sera armazenado 
	 * @throws Exception 
	 */
	protected void store(LinkedProperties properties, File file) throws Exception {		
		try { 
			properties.store(new FileOutputStream(file), null);
		}
		catch (Exception e) {
			throw new Exception(MessageFormat.format("Erro ao persistir o arquivo: {0}, mensagem interna: {1}", file.getName(), e.getMessage())); 
		}
	}
	
	/**
	 * Carrega um arquivo de propriedade
	 * 
	 * @param is A fonte do arquivo que sera carregado
	 * @return Os dados do arquivo de propriedade
	 * @throws Exception 
	 */
	protected LinkedProperties load(InputStream is, String name) throws Exception {
		try {
			LinkedProperties properties = new LinkedProperties();
			
			properties.load(is);

			return properties;
		}
		catch (Exception e) {
			throw new Exception(MessageFormat.format("Erro ao carregar o arquivo de propriedade: {0}, mensagem interna: {1}", name, e.getMessage()));
		}
	}
}
