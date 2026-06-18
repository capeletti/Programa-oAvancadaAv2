package model;

public enum Permissao {
	ABRIR_TICKET("Abrir ticket"),
	RESPONDER_TICKET("Responder ticket"),
	FECHAR_TICKET("Fechar ticket"),
	CADASTRAR_USUARIO("Cadastrar usuário"),
	CADASTRAR_FUNCAO("Cadastrar função");
	
    private final String descricao;

	Permissao(String descricao) {
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
