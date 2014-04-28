package br.com.atos.gc.model;

import br.com.atos.gc.GeradorCodigo;
import br.com.atos.utils.ReflectionUtils;
import br.com.atos.utils.StringUtils;
import br.com.atosdamidia.comuns.modelo.IBaseEntity;
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

public class Entity {

	private Class<? extends IBaseEntity<?>> clazz;
	private boolean inicializedAttributes;
	private boolean initializedLabelAndGender;
	private Gender gender;
	private String label;
	private GeradorCodigo gc;	
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
	public Entity(Class<? extends IBaseEntity<?>> clazz, GeradorCodigo gc) {
		
		this.clazz = clazz;		
		this.gc = gc;		
		
		loadProperties();
				
		initializeAttributes();
	}
	
	private void loadProperties() {

		// Verifica se possui os dados no gc.properties
		String genderValue = gc.getGcProperties().getProperty(getClazz().getName() + ".gender");

		if (!StringUtils.isNullOrEmpty(genderValue)) {
			gender = Gender.valueOf(genderValue.toUpperCase());
		}
		
		String labelValue = gc.getMessagesProperties().getProperty(getClazz().getName());
		
		if (!StringUtils.isNullOrEmpty(labelValue)) {
			label = labelValue;
		}
		
		if (gender != null && label != null) {
			initializedLabelAndGender = true;
		}
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}

	public Class<?> getClazz() {
		return clazz;
	}
	
	public String getGender() {
		return gender.getDescription();
	}

	public String getLabel() {
		return label;
	}	
	
	public GeradorCodigo getGc() {
		return gc;
	}

	public boolean isInicializedAttributes() {
		return inicializedAttributes;
	}

	public boolean isInicializedLabelAndGender() {
		return initializedLabelAndGender;
	}

	public void initializeLabelsAndGenderIfNecessarily() {

		if (!isInicializedLabelAndGender()) {

			String entityLabel = null;
	
			while (StringUtils.isNullOrEmpty(entityLabel)) {
				entityLabel = JOptionPane.showInputDialog(MessageFormat.format("Digite um rotulo para a entidade {0}.", getClazz().getSimpleName()));	
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

			gender = substantivoMasculino ? Gender.M : Gender.F;
			label = entityLabel;			
			initializedLabelAndGender = true;
		}
	}
	
	public boolean isAttributeIgnored(String atributoNome) {
		for (String atributoIgnorado : getGc().getAtributosIgnorados()) {
			if (atributoNome.equals(atributoIgnorado)) {
				return true;
			}
		}
		return false;
	}
	
	private void initializeAttributes() {

		List<Field> fields = ReflectionUtils.getFieldsRecursive(getClazz());

		for (Field field : fields) {
	
			if (!Modifier.isStatic(field.getModifiers()) && !isAttributeIgnored(field.getName())) {
									
				if (field.getAnnotation(Id.class) != null) {
					attributes.add(new AttributeId(field, this));
				}
				else {
					if (field.getAnnotation(OneToMany.class) != null) {
						attributes.add(new AttributeOneToMany(field, this));
					}
					else if (field.getAnnotation(ManyToOne.class) != null || field.getAnnotation(OneToOne.class) != null) {
						attributes.add(new AttributeManyToOne(field, this));
					}
					else {
						attributes.add(new Attribute(field, this));
					}
				}
			}
		}
	}
	
	public void inicializarAtributosSeNecessario() {
		
		if (!isInicializedAttributes()) {

			for (Attribute attribute : getAttributes()) {
				
				if (!(attribute instanceof AttributeId)) {
					
					String label = JOptionPane.showInputDialog("Digite um label para o atributo (" + attribute.getField().getName() + "):");
					
					if (StringUtils.isNullOrEmpty(label)) {
						label = attribute.getField().getName();
					}
					
					attribute.setLabel(label);
					
					if (attribute instanceof AttributeOneToMany) {
						
						AttributeFormType formType = null;
						
						while (formType == null) {
							formType = (AttributeFormType) JOptionPane.showInputDialog(null,
									"Qual o tipo de formulário para o atributo (" + label + ")?", 
									"Tipo de Formulário",
									JOptionPane.INFORMATION_MESSAGE, 
									null,
									AttributeFormType.values(), 
									AttributeFormType.EXTERNAL
							);
						}
						
						((AttributeOneToMany) attribute).setFormType(formType);
					}
					else if (attribute instanceof AttributeManyToOne) {
						
						Field associationAttributeField = null;
						String associationAttributeDescription = null;
						
						while (StringUtils.isNullOrEmpty(associationAttributeDescription)) {								
							
							associationAttributeDescription = JOptionPane.showInputDialog("Digite o atributo da associação (" + attribute.getField().getName() + ") que será utilizado como descrição:");
							
							// Verifica se este atributo existe na associacao
							List<Field> associationFields = ReflectionUtils.getFieldsRecursive(attribute.getField().getType());

							for (Field associacaoField : associationFields) {
								if (associacaoField.getName().equals(associationAttributeDescription)) {
									associationAttributeField = associacaoField;
									break;
								}
							}
							
							if (associationAttributeField == null) {
								associationAttributeDescription = null;
							}
						}

						((AttributeManyToOne) attribute).setAssociationAttributeDescription(associationAttributeDescription);
						((AttributeManyToOne) attribute).setAssociationAttributeField(associationAttributeField);
					}
				}
			}
			inicializedAttributes = true;
		}		
	}
	
	public String getClazzSimpleName() {
		return getClazz().getSimpleName();
	}
	
	public List<AttributeManyToOne> getAtributosManyToOne() {
		
		List<AttributeManyToOne> atributosManyToOne = new ArrayList<AttributeManyToOne>();
		
		for (Attribute atributo : getAttributes()) {
			if (atributo instanceof AttributeManyToOne) {
				atributosManyToOne.add((AttributeManyToOne) atributo);
			}
		}
		
		return atributosManyToOne;	
	}
	
	public List<AttributeOneToMany> getAtributosOneToMany() {
		
		List<AttributeOneToMany> atributosOneToMany = new ArrayList<AttributeOneToMany>();
		
		for (Attribute atributo : getAttributes()) {
			if (atributo instanceof AttributeOneToMany) {
				atributosOneToMany.add((AttributeOneToMany) atributo);
			}
		}
		
		return atributosOneToMany;	
	}
	
	public AttributeId getAtributoId() {
		
		for (Attribute atributo : getAttributes()) {
			if (atributo instanceof AttributeId) {
				return (AttributeId) atributo;
			}
		}
		
		return null;
	}
	
	public boolean isAuditada() {
		return getClazz().isAnnotationPresent(Audited.class);
	}
}