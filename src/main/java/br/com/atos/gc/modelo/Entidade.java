package br.com.atos.gc.modelo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.swing.JOptionPane;

import org.hibernate.envers.Audited;

import br.com.atos.utils.ReflectionUtils;
import br.com.atos.utils.StringUtils;
import br.com.atosdamidia.comuns.modelo.IBaseEntity;

public class Entidade {

	private Class<? extends IBaseEntity<?>> tipo;
	private boolean inicializadoAtributos;
	private boolean inicializadoRotuloEhArtigo;
	private String artigoDefinido;
	private String rotulo;	
		
	public Entidade(Class<? extends IBaseEntity<?>> tipo) {
		super();
		this.tipo = tipo;
	}

	private List<Atributo> atributos = new ArrayList<Atributo>();

	public List<Atributo> getAtributos() {
		return atributos;
	}

	public Class<?> getTipo() {
		return tipo;
	}
	
	public String getArtigoDefinido() {
		return artigoDefinido;
	}

	public String getRotulo() {
		return rotulo;
	}

	public boolean isInicializadoAtributos() {
		return inicializadoAtributos;
	}

	public boolean isInicializadoRotuloEhArtigo() {
		return inicializadoRotuloEhArtigo;
	}

	public void inicializarRotuloEhArtigoSeNecessario() {

		if (!isInicializadoRotuloEhArtigo()) {

			String entidadeRotulo = null;
	
			while (StringUtils.isNullOrEmpty(entidadeRotulo)) {
				entidadeRotulo = JOptionPane.showInputDialog(MessageFormat.format("Digite um rotulo para a entidade {0}.", getTipo().getSimpleName()));	
			}
			
			String m = "Masculino";
			String f = "Feminino";
			
			String selectedValue = null;
			
			while (selectedValue == null) {
				selectedValue = (String) JOptionPane.showInputDialog(null,
						"O nome da entidade é um substantivo?", 
						"Substantivo",
						JOptionPane.INFORMATION_MESSAGE, 
						null,
						new String[]{m,f}, 
						m
				);
			}

			boolean substantivoMasculino = true;

			if (selectedValue == f) {
				substantivoMasculino = false; 
			}

			artigoDefinido = substantivoMasculino ? "o" : "a";
			rotulo = entidadeRotulo;			
			inicializadoRotuloEhArtigo = true;
		}
	}
	
	public void inicializarAtributosSeNecessario() {
		
		if (!isInicializadoAtributos()) {

			List<Field> fields = ReflectionUtils.getFieldsRecursive(getTipo());
			
			for (Field field : fields) {
	
				if (!Modifier.isStatic(field.getModifiers())) {
					
					if (field.getAnnotation(Id.class) != null) {
						atributos.add(new AtributoId(field));
					}
					else {
						
						String rotulo = JOptionPane.showInputDialog("Digite um rotulo para o atributo (" + field.getName() + "):");
						
						if (StringUtils.isNullOrEmpty(rotulo)) {
							rotulo = field.getName();
						}
						
						if (field.getAnnotation(OneToMany.class) != null) {

							AtributoTipoFormulario tipoFormulario = null;
							
							while (tipoFormulario == null) {
								tipoFormulario = (AtributoTipoFormulario) JOptionPane.showInputDialog(null,
										"Qual o tipo de formulário para o atributo (" + rotulo + ")?", 
										"Tipo de Formulário",
										JOptionPane.INFORMATION_MESSAGE, 
										null,
										AtributoTipoFormulario.values(), 
										AtributoTipoFormulario.EXTERNO
								);
							}
							
							atributos.add(new AtributoOneToMany(field, rotulo, tipoFormulario));
						}
						else if (field.getAnnotation(ManyToOne.class) != null || field.getAnnotation(OneToOne.class) != null) {
							
							Field associacaoAtributoField = null;
							String associacaoAtributoDescricao = null;
							
							while (StringUtils.isNullOrEmpty(associacaoAtributoDescricao)) {								
								
								associacaoAtributoDescricao = JOptionPane.showInputDialog("Digite o atributo da associação (" + field.getName() + ") que será utilizado como descrição:");
								
								// Verifica se este atributo existe na associacao
								List<Field> associacaoFields = ReflectionUtils.getFieldsRecursive(field.getType());

								for (Field associacaoField : associacaoFields) {
									if (associacaoField.getName().equals(associacaoAtributoDescricao)) {
										associacaoAtributoField = associacaoField;
										break;
									}
								}
								
								if (associacaoAtributoField == null) {
									associacaoAtributoDescricao = null;
								}
							}
							
							atributos.add(new AtributoManyToOne(field, rotulo, associacaoAtributoField, associacaoAtributoDescricao));
						}
						else {
							atributos.add(new Atributo(field, rotulo));
						}
					}
				}
			}
			
			inicializadoAtributos = true;
		}		
	}
	
	public String getTipoSimpleName() {
		return getTipo().getSimpleName();
	}
	
	public List<AtributoManyToOne> getAtributosManyToOne() {
		
		List<AtributoManyToOne> atributosManyToOne = new ArrayList<AtributoManyToOne>();
		
		for (Atributo atributo : getAtributos()) {
			if (atributo instanceof AtributoManyToOne) {
				atributosManyToOne.add((AtributoManyToOne) atributo);
			}
		}
		
		return atributosManyToOne;	
	}
	
	public List<AtributoOneToMany> getAtributosOneToMany() {
		
		List<AtributoOneToMany> atributosOneToMany = new ArrayList<AtributoOneToMany>();
		
		for (Atributo atributo : getAtributos()) {
			if (atributo instanceof AtributoOneToMany) {
				atributosOneToMany.add((AtributoOneToMany) atributo);
			}
		}
		
		return atributosOneToMany;	
	}
	
	public AtributoId getAtributoId() {
		
		for (Atributo atributo : getAtributos()) {
			if (atributo instanceof AtributoId) {
				return (AtributoId) atributo;
			}
		}
		
		return null;
	}
	
	public boolean isAuditada() {
		return getTipo().isAnnotationPresent(Audited.class);
	}
}