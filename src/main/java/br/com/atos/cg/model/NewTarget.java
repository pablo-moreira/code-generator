package br.com.atos.cg.model;

public class NewTarget {

	/**
	 * O nome do target
	 * Exemplo: DAO
	 */
	private String name;
	
	/**
	 * Descrição do que target ira fazer
	 * Exemplo: Gerar o arquivo DAO
	 */
	private String description;
	
	/** 
	 * Se verdadeiro o arquivo pode ser sobrescrito
	 */
	private boolean allowOverwrite;
	
	/**
	 * O nome do arquivo e diretorio onde o arquivo sera criado aceita el 
	 */
	private String filename;
	
	/** 
	 * O nome do arquivo template que sera utilizado na execucao do target
	 */
	private String template;

	public NewTarget(String name, String description, boolean allowOverwrite, String filename, String template) {
		this.name = name;
		this.description = description;
		this.allowOverwrite = allowOverwrite;
		this.filename = filename;
		this.template = template;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAllowOverwrite() {
		return allowOverwrite;
	}

	public void setAllowOverwrite(boolean allowOverwrite) {
		this.allowOverwrite = allowOverwrite;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
}