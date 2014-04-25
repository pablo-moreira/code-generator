package br.com.atos.gc.modelo;

import java.lang.reflect.Field;


public class AtributoManyToOne extends Atributo {
	
	private Field associacaoAtributoField;
	private String associacaoAtributoDescricao;
		
	public AtributoManyToOne(Field atributo, String label, Field associacaoAtributoField, String associacaoAtributoDescricao) {
		super(atributo, label);
		this.associacaoAtributoField = associacaoAtributoField;
		this.associacaoAtributoDescricao = associacaoAtributoDescricao;
	}

	public String getAssociacaoAtributoDescricao() {
		return associacaoAtributoDescricao;
	}
	
	public Field getAssociacaoAtributoField() {
		return associacaoAtributoField;
	}	
}
