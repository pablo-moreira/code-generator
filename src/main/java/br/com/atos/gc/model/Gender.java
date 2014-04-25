package br.com.atos.gc.model;

public enum Gender {
	
	M("o"),
	F("a");
	
	private String description;

	private Gender(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
