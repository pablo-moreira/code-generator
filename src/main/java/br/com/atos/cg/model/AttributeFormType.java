package br.com.atos.cg.model;

public enum AttributeFormType { 
	
    EXTERNAL("Externo")
    , INTERNAL("Interno");
    
    private final String description;

    private AttributeFormType(String descricao) {
        this.description = descricao;    
    }

    public String getDescription() {
        return description;
    }
}