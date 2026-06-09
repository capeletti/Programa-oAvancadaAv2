package model;

public enum Setor {

    ADMINISTRACAO("Administração"),
    COMERCIAL("Comercial"),
    COMPRAS("Compras"),
    FINANCEIRO("Financeiro"),
    MARKETING("Marketing"),
    PRODUCAO("Produção"),
    RH("RH"),
    TI("TI");

    private final String descricao;

    Setor(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
    
    @Override
    public String toString() {
        return descricao;
    }
}