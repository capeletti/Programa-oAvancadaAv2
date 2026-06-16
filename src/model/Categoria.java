package model;

public enum Categoria {

    DUVIDA("Dúvida"),
    SOLICITACAO("Solicitação"),
    INCIDENTE("Incidente"),
    MELHORIA("Melhoria");

    private final String descricao;

    Categoria(String descricao) {
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
