package com.github.cg.manager;

import java.io.File;
import java.io.FileInputStream;

import com.github.cg.util.LinkedProperties;

public class MessagesPropertiesManager extends PropertiesManager {

	public static final String MESSAGES_PROPERTIES_FILENAME = "messages.properties";
	
	public MessagesPropertiesManager(ManagerRepository repository) {
		super(repository);
	}

	/**
	 * Persiste o arquivo de mensagens e carrega o arquivo novamente com as modificacoes
	 * 
	 * @param messagesProperties O arquivo de mensagens
	 * @param dirResources O diretorio onde o arquivo de propriedades sera armazenado
	 * @return As mensagens atualizadas
	 * @throws Exception
	 */
	public LinkedProperties storeAndLoad(LinkedProperties messagesProperties, File dirResources) throws Exception {

		File messagesFile = new File(dirResources, MESSAGES_PROPERTIES_FILENAME);
		
		store(messagesProperties, messagesFile);
				
		return load(new FileInputStream(messagesFile), MESSAGES_PROPERTIES_FILENAME);
	}
	
	/**
	 * Carrega o arquivo de mensagens 
	 * @return As mensagens
	 * @throws Exception
	 */
	public LinkedProperties loadMessagesProperties(File dirResources) throws Exception {
		
		File messagesFile = new File(dirResources, MESSAGES_PROPERTIES_FILENAME);
		
		// Se nao encontrar o arquivo messages.properties cria
		if (messagesFile.exists() == false) {		
			messagesFile.createNewFile();
		}
		
		return load(new FileInputStream(messagesFile), MESSAGES_PROPERTIES_FILENAME);
	}
}