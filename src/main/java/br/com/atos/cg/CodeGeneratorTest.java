package br.com.atos.cg;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

public class CodeGeneratorTest {

	public static void main(String args[]) throws Exception {
		
		// inicializando o velocity
		VelocityEngine ve = new VelocityEngine();
				
		URL url = CodeGeneratorTest.class.getResource("/cg.templates/");

		File file = new File(url.getFile());

		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
		ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, file.getAbsolutePath());
		ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, "true");
				
		ve.init();

		// criando o contexto que liga o java ao template
		VelocityContext context = new VelocityContext();
		
		// escolhendo o template
		Template t = ve.getTemplate("br.com.atos.template.vm");
		
		// variavel que sera acessada no template:
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);

		// aqui! damos a variavel list para
		// o contexto!
		context.put("lista", list);	
		StringWriter writer = new StringWriter();

		// mistura o contexto com o template
		t.merge(context, writer);

		System.out.println(writer.toString());
	}	
}
