package br.com.atos.gc.modelo;

import java.lang.reflect.Field;

public class AtributoId extends Atributo {

	public AtributoId(Field atributo) {
		super(atributo, "Id");
		setGridFiltro(true);
		setGridColuna(true);
	}	
}
