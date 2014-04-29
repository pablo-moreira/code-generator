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
		
		load();
				
		initializeAttributes();
	}
	
	private void load() {

		// Verifica se possui os dados no gc.properties
		String genderValue = gc.getGcProperties().getProperty(getClazz().getName() + ".gender");

		if (!StringUtils.isNullOrEmpty(genderValue)) {
			gender = Gender.valueOf(genderValue);
		}
		
		String labelValue = gc.getMessagesProperties().getProperty(getClazz().getName());
		
		if (!StringUtils.isNullOrEmpty(labelValue)) {
			label = labelValue;
		}
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}

	public Class<?> getClazz() {
		return clazz;
	}
	
	public Gender getGender() {
		return gender;
	}
        
    public String getGenderDescription() {
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
	
	public boolean isAttributeIgnored(String attributeName) {
		for (String attributeIgnored : getGc().getAtributosIgnorados()) {
			if (attributeName.equals(attributeIgnored)) {
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

						((AttributeManyToOne) attribute).setDescriptionAttributeOfAssociation(associationAttributeDescription);
					}
				}
			}
			inicializedAttributes = true;
		}		
	}
	
	public String getClazzSimpleName() {
		return getClazz().getSimpleName();
	}
	
	public List<AttributeManyToOne> getAttributesManyToOne() {
		
		List<AttributeManyToOne> attributesManyToOne = new ArrayList<AttributeManyToOne>();
		
		for (Attribute attribute : getAttributes()) {
			if (attribute instanceof AttributeManyToOne) {
				attributesManyToOne.add((AttributeManyToOne) attribute);
			}
		}
		
		return attributesManyToOne;	
	}
	
	public List<AttributeOneToMany> getAttributesOneToMany() {
		
		List<AttributeOneToMany> attributesOneToMany = new ArrayList<AttributeOneToMany>();
		
		for (Attribute attribute : getAttributes()) {
			if (attribute instanceof AttributeOneToMany) {
				attributesOneToMany.add((AttributeOneToMany) attribute);
			}
		}
		
		return attributesOneToMany;	
	}
	
	public AttributeId getAttributeId() {
		
		for (Attribute attribute : getAttributes()) {
			if (attribute instanceof AttributeId) {
				return (AttributeId) attribute;
			}
		}
		
		return null;
	}
	
	public boolean isAudited() {
		return getClazz().isAnnotationPresent(Audited.class);
	}

	public void store() {
		
		String keyBase = getClazz().getName();
				
		if (getGender() != null) {
			// Verifica se possui os dados no gc.properties
			gc.getGcProperties().setProperty(keyBase  + ".gender", getGender().name());
		}

		if (getLabel() != null) {
			gc.getMessagesProperties().setProperty(keyBase, getLabel());
		}
	
		for (Attribute attribute : getAttributes()) {			
			attribute.store();						
		}
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLabelDefault() {
		this.label = getClazzSimpleName();
	}	
}