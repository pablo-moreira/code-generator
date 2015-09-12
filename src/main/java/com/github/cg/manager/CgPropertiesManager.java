package com.github.cg.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.github.cg.component.StringUtils;
import com.github.cg.util.LinkedProperties;

public class CgPropertiesManager extends PropertiesManager {
	
	private static final Logger logger = Logger.getLogger(CgPropertiesManager.class);
	
	public static final String CG_PROPERTIES_FILENAME = "cg.properties";
	public static final String DIRS_SRC = "dirs.src";
	public static final String DIRS_RESOURCES = "dirs.resources";
	public static final String DIRS_WEBCONTENT = "dirs.web";

	public CgPropertiesManager(ManagerRepository repository) {
		super(repository);
	}

	/**
	 * Verifica se as propriedades requeridas foram declaradas no arquivo cg.properties
	 * @param cgProperties
	 * @param requiredProperties 
	 * @throws Exception 
	 */
	public void verifyRequiredProperties(LinkedProperties cgProperties, List<String> requiredProperties) throws Exception {
		
		List<String> requiredPropertiesCopy = new ArrayList<String>(requiredProperties);
		
		Iterator<Entry<String, String>> iterator = cgProperties.iterator();
				
		while (iterator.hasNext()) {
			
			Entry<String, String> entry = iterator.next();
			
			if (requiredPropertiesCopy.contains(entry.getKey())) {
				requiredPropertiesCopy.remove(entry.getKey());
			}
		}
		
		if (requiredPropertiesCopy.size() > 0) {
			for (String requiredProperty : requiredPropertiesCopy) {
				logger.error(MessageFormat.format("A propriedade {0} requerida não foi declarada no arquivo cg.properties", requiredProperty));
			}
			throw new Exception(MessageFormat.format("Propriedades requeridas não foram declaradas: {0}", StringUtils.getInstance().join(requiredPropertiesCopy, ", ")));
		}
	}

	/**
	 * Carrega o arquivo de propriedades do gerador de codigo da aplicacao
	 * @param requiredProperties As propriedades que sao requeridas pelos plugins 
	 * @return As propriedades do Gerador de Codigo
	 * @throws Exception
	 */
	public LinkedProperties loadCgProperties(List<String> requiredProperties) throws Exception {

		// Tenta recuperar o gc.properties pelo classPath
		InputStream is = this.getClass().getResourceAsStream("/" + CG_PROPERTIES_FILENAME);
		
		if (is == null) {
			throw new RuntimeException("O arquivo cg.properties não foi encontrado no classpath!");
		}

		LinkedProperties cgProperties;
		
		cgProperties = load(is, CG_PROPERTIES_FILENAME);
		
		verifyRequiredProperties(cgProperties, requiredProperties);
		
		return cgProperties;
	}

	/**
	 * Persiste as propriedades do gerador de codigo e carrega o arquivo novamente com as modificacoes
	 * 
	 * @param cgProperties As propriedades do Gerador do Codigo
	 * @param dirResources O diretorio onde o arquivo de propriedades sera armazenado
	 * @return As propriedades do Gerador de Codigo Atualizadas
	 * @throws Exception
	 */
	public LinkedProperties storeAndLoad(LinkedProperties cgProperties, File dirResources) throws Exception {

		File file = new File(dirResources, CG_PROPERTIES_FILENAME);
		
		store(cgProperties, file);
		
		return load(new FileInputStream(file), CG_PROPERTIES_FILENAME);
	}
}