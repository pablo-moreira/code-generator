package com.github.cg.manager;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import br.com.atos.cg.CodeGenerator;

public class TemplateManager extends BaseManager {

	private VelocityEngine velocityEngine;

	public TemplateManager(ManagerRepository repository) {
		super(repository);
	}

	public String mergeStringTemplate(VelocityContext context, String templateString) {
		
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

	public void mergeFileTemplate(VelocityContext context, String templateName, File file) {

		Template template = getVelocityEngine().getTemplate(templateName);
		
		PrintWriter writer;
		
		try {
			writer = new PrintWriter(file);
		}
		catch (Exception e) {
			throw new RuntimeException("Não foi possível encontrar o arquivo, mensagem interna: " + e.getMessage());
		}
			
		try {
			template.merge(context, writer);
		}
		catch (Exception e) {
			throw new RuntimeException("Erro ao mesclar o template, mensagem interna: " + e.getMessage());
		}
				
		writer.close();		
	}
	
	private VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void initVelocityEngine() {

		velocityEngine = new VelocityEngine();
			
		URL url = CodeGenerator.class.getResource("/com.github.cg.templates");

		File file = new File(url.getFile());

		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
		velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, file.getAbsolutePath());
		velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, "true");						
		velocityEngine.init();		
	}
}
