package com.github.cg.annotation;

public @interface Target {
	
	/**
	 * O nome do target
	 * Exemplo: DAO
	 */
	public abstract String name();
	
	/**
	 * Descrição do que target ira fazer
	 * Exemplo: Gerar o arquivo DAO
	 */
	public abstract String description();
	
	/** 
	 * Se verdadeiro o arquivo pode ser sobrescrito
	 */
	public abstract boolean allowOverwrite();

	/**
	 * O nome do diretorio e do arquivo que sera criado. Aceita el 
	 */
	public abstract String filename();
	
	/** 
	 * O nome do diretorio e do arquivo template que sera utilizado na execucao do target
	 */
	public abstract String template();
	
	/**
	 * Armazena as tarefas que serao executada antes da execucao do target
	 */
	public abstract TargetTask[] tasksToExecuteBefore() default {};
	
	/**
	 * Armazena as tarefas que serao executada apos a execucao do target
	 */
	public abstract TargetTask[] tasksToExecuteAfter() default {};
	
}
