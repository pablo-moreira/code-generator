package br.com.atos.gc.modelo;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.OneToMany;

import br.com.atos.utils.StringUtils;

public class AtributoOneToMany extends Atributo {
	
	public AtributoOneToMany(Field atributo, String label, AtributoTipoFormulario tipoFormulario) {
		super(atributo, label);
		this.tipoFormulario = tipoFormulario;
	}
	
	private AtributoTipoFormulario tipoFormulario;

	public AtributoTipoFormulario getTipoFormulario() {
		return tipoFormulario;
	}

	public void setTipoFormulario(AtributoTipoFormulario tipoFormulario) {
		this.tipoFormulario = tipoFormulario;
	}
	
	public String getAssociacaoTipoSimpleName() {
		try {
			return getAssociacaoTipo().getSimpleName();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public Class<?> getAssociacaoTipo() {
		try {
			return Class.forName(getAssociacaoTipoName());
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public String getAssociacaoTipoName() {
		
		Type type = getField().getGenericType();
		
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type; 
			Type[] actualTypeArguments = pt.getActualTypeArguments();
			if (actualTypeArguments != null && actualTypeArguments.length > 0) {
				String aux = actualTypeArguments[0].toString();
				try {
					return aux.replace("class ", "");
				}
				catch (Exception e) {}
			}			
		}

		return getField().getType().getName();
	}

	public String getAssociacaoMappedBy() {
		
		String mappedBy = "";
		
		if (getField().getAnnotation(OneToMany.class) != null) {
			mappedBy = StringUtils.firstToUpperCase(getField().getAnnotation(OneToMany.class).mappedBy());
		}
		
		return mappedBy;
	}
}