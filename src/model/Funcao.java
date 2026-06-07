package model;

import java.util.ArrayList;
import java.util.List;

public class Funcao {
	private int id;
	private String nome;
	private ArrayList<Permissao> permissoes;
	private boolean ativo;
	
	public Funcao() {
		this.permissoes = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ArrayList<Permissao> getPermissoes() {
	    return permissoes;
	}

	public void setPermissoes(List<Permissao> novasPermissoes) {
		this.permissoes.clear();
		if(novasPermissoes == null) {
			return;
		}
		for(int i = 0; i < novasPermissoes.size(); i++) {
			Permissao p = novasPermissoes.get(i);
			this.adicionarPermissao(p);
		}
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	public boolean temPermissao(Permissao p) {
		return this.permissoes.contains(p);
	}
	
	public void adicionarPermissao(Permissao p) {
		if(!this.permissoes.contains(p)) {
			this.permissoes.add(p);
		}
	}
	
	@Override
	public boolean equals(Object obj) {

	    if (this == obj) {
	        return true;
	    }

	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }

	    Funcao outra = (Funcao) obj;

	    return this.id == outra.id;
	}

	@Override
	public int hashCode() {
	    return Integer.hashCode(id);
	}
	
	@Override
	public String toString() {
	    return nome;
	}
	
}
