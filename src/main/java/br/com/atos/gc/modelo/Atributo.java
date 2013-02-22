package br.com.atos.gc.modelo;

import java.lang.reflect.Field;

public class Atributo {

	private Field field;	
	private String rotulo;
	
	private boolean gridColuna = true;
	private boolean gridFiltro = true;

	public Atributo(Field field, String rotulo) {
		super();
		this.field = field;
		this.rotulo = rotulo;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getRotulo() {
		return rotulo;
	}

	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	public boolean isGridColuna() {
		return gridColuna;
	}

	public void setGridColuna(boolean gridColuna) {
		this.gridColuna = gridColuna;
	}

	public boolean isGridFiltro() {
		return gridFiltro;
	}

	public void setGridFiltro(boolean gridFiltro) {
		this.gridFiltro = gridFiltro;
	}
}
