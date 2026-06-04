package test;

import java.util.ArrayList;
import model.BD;
import model.Funcao;
import model.FuncaoDAO;
import model.Permissao;
import model.TipoOperacaoBD;

public class FuncaoDAOTeste {

    public static void main(String[] args) {

        // Testa conexão
        BD bd = new BD();
        if (bd.connect()) {
            System.out.println("Conexão OK!");
            bd.close();
        } else {
            System.out.println("FALHA NA CONEXÃO - verifique host, porta e credenciais.");
            return;
        }

        // Testa inclusão
        Funcao nova = new Funcao();
        nova.setNome("Função Teste");
        nova.setAtivo(true);
        nova.adicionarPermissao(Permissao.ABRIR_TICKET);
        nova.adicionarPermissao(Permissao.FECHAR_TICKET);

        FuncaoDAO dao = new FuncaoDAO(nova);
        String resultado = dao.atualizar(TipoOperacaoBD.INCLUSAO);
        System.out.println("Inclusão: " + resultado);
        System.out.println("ID gerado: " + nova.getId());

        // Testa listagem
        FuncaoDAO dao2 = new FuncaoDAO(new Funcao());
        ArrayList<Funcao> lista = dao2.listarTodas();
        System.out.println("Total de funções no banco: " + lista.size());

        for (int i = 0; i < lista.size(); i++) {
            Funcao f = lista.get(i);
            System.out.println("  - [" + f.getId() + "] " + f.getNome()
                + " | Ativo: " + f.isAtivo()
                + " | Permissões: " + f.getPermissoes());
        }
    }
}