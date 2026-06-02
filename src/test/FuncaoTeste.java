package test;

import java.util.ArrayList;
import java.util.List;
import model.Funcao;
import model.Permissao;

public class FuncaoTeste {

	public static void main(String[] args) {
		
		Funcao funcaoGerente = new Funcao();
		funcaoGerente.setNome("Gerente de Suporte");
		
		List<Permissao> listaBaguncada = new ArrayList<>();
		listaBaguncada.add(Permissao.ABRIR_TICKET);
		listaBaguncada.add(Permissao.ABRIR_TICKET);
		listaBaguncada.add(Permissao.FECHAR_TICKET);
		listaBaguncada.add(Permissao.ABRIR_TICKET);
		
		funcaoGerente.setPermissoes(listaBaguncada);
		
		System.out.println("Permissoes reais salvas na Funcao:");
		
		List<Permissao> listaFiltrada = funcaoGerente.getPermissoes();
		
		for (int i = 0; i < listaFiltrada.size(); i++) {
			System.out.println("- " + listaFiltrada.get(i));
		}
	}
}