package br.com.atos.gc.model;

import java.lang.reflect.Field;


public class AttributeManyToOne extends Attribute {

	/*
	 * Refatorar para armazenar uma expression language para o atributo de descricao da associacao
	 * descriptionAttributeOfAssociation
	 */
	private Field associationDescriptionField;
	private String associationDescriptionFieldName; // Remover nao serve para nada.
		
	public AttributeManyToOne(Field field, String label, Field associationAttributeField, String associationAttributeDescription) {
		super(field, label);
		this.associationDescriptionField = associationAttributeField;
		this.associationDescriptionFieldName = associationAttributeDescription;
	}

	public AttributeManyToOne(Field field, Entity entity) {
		super(field, entity);
	}

	public String getAssociationAttributeDescription() {
		return associationDescriptionFieldName;
	}
	
	public Field getAssociationAttributeField() {
		return associationDescriptionField;
	}

	public void setAssociationAttributeField(Field associationAttributeField) {
		this.associationDescriptionField = associationAttributeField;
	}

	public void setAssociationAttributeDescription(String associationAttributeDescription) {
		this.associationDescriptionFieldName = associationAttributeDescription;
	}	
}
