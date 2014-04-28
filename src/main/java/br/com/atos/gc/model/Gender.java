package br.com.atos.gc.model;

public enum Gender {
	
	M("Masculino"),
	F("Feminino");
	
	private String description;

	private Gender(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
