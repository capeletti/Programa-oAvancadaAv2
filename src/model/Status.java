package model;

public enum Status {

    ABERTO("Aberto"),
    EM_ANDAMENTO("Em andamento"),
    FECHADO("Fechado"),
    CANCELADO("Cancelado");

    private final String descricao;

    Status(String descricao) {
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
