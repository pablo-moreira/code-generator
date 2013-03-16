package br.com.atos.gc.modelo;

import java.lang.reflect.Field;

public class Atributo {

	private Field field;	
	private String rotulo;
	
	private boolean renderizarGridColuna = true;
	private boolean renderizarGridFiltro = true;
	private boolean renderizarWinFrm = true;

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

	public boolean isRenderizarGridColuna() {
		return renderizarGridColuna;
	}

	public void setRenderizarGridColuna(boolean renderizarGridColuna) {
		this.renderizarGridColuna = renderizarGridColuna;
	}

	public boolean isRenderizarGridFiltro() {
		return renderizarGridFiltro;
	}

	public void setRenderizarGridFiltro(boolean renderizarGridFiltro) {
		this.renderizarGridFiltro = renderizarGridFiltro;
	}

	public boolean isRenderizarWinFrm() {
		return renderizarWinFrm;
	}

	public void setRenderizarWinFrm(boolean renderizarFrm) {
		this.renderizarWinFrm = renderizarFrm;
	}
}
