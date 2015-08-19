package com.github.cg.manager;

import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import com.github.cg.util.FileUtils;

public class TemplateManager extends BaseManager {

	public TemplateManager(ManagerRepository repository) {
		super(repository);
	}

	public String mergeTemplateString(VelocityContext context, String templateString) {
		
		StringWriter writer = new StringWriter();
		
		mergeTemplateString(context, templateString, writer);

		return writer.toString();
	}
	
	private void mergeTemplateString(VelocityContext context, String templateString, Writer writer) {
		
		try {			
			RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
			StringReader reader = new StringReader(templateString);
			SimpleNode node = runtimeServices.parse(reader, "template");
			
			Template template = new Template();
			template.setRuntimeServices(runtimeServices);
			template.setData(node);
			template.initDocument();
			template.merge(context, writer);
		}
		catch (Exception e) {
			throw new RuntimeException("Erro ao mesclar o template, mensagem interna: " + e.getMessage());
		}		
	}

	public void mergeTemplateFile(VelocityContext context, String templateFilename, File file) {
		
		InputStream is = TemplateManager.class.getResourceAsStream(templateFilename);

		String templateString = FileUtils.getStringFromInputStream(is);
		
		PrintWriter writer;
		
		try {
			writer = new PrintWriter(file);
		}
		catch (Exception e) {
			throw new RuntimeException("Não foi possível encontrar o arquivo, mensagem interna: " + e.getMessage());
		}
			
		try {
			mergeTemplateString(context, templateString, writer);
		}
		catch (Exception e) {
			throw new RuntimeException("Erro ao mesclar o template, mensagem interna: " + e.getMessage());
		}
		
		writer.close();
	}
}
