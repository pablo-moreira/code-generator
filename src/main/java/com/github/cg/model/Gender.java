package com.github.cg.model;

public enum Gender {
	
	M("Masculino", "o"),
	F("Feminino", "a");
	
	private String description;
	private String article;

	private Gender(String description, String article) {
		this.description = description;
        this.article = article;
	}

	public String getDescription() {
		return description;
	}

    public String getArticle() {
        return article;
    }
}
