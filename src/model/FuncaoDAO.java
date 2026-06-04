package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class FuncaoDAO implements OperacaoBD {

    private Funcao funcao;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private String sql;

    public FuncaoDAO(Funcao funcao) {
        this.funcao = funcao;
    }

    // Localizar pelo id
    @Override
    public boolean localizar() {
        BD bd = new BD();
        if (!bd.connect()) return false;

        try {
            sql = "SELECT id_funcao, nome, ativo FROM funcao WHERE id_funcao = ?";
            statement = bd.getConnection().prepareStatement(sql);
            statement.setInt(1, funcao.getId());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                funcao.setId(resultSet.getInt("id_funcao"));
                funcao.setNome(resultSet.getString("nome"));
                funcao.setAtivo(resultSet.getBoolean("ativo"));
                funcao.setPermissoes(buscarPermissoes(funcao.getId(), bd));
                return true;
            }
            return false;

        } catch (SQLException erro) {
            System.out.println("Erro ao localizar Funcao: " + erro.getMessage());
            return false;
        } finally {
            bd.close();
        }
    }

    public boolean localizar(int id) {
        funcao.setId(id);
        return localizar();
    }

    // Incluir, alterar e excluir

    @Override
    public String atualizar(TipoOperacaoBD operacao) {
        BD bd = new BD();
        if (!bd.connect()) return "Erro: falha ao conectar ao banco de dados.";

        try {
            switch (operacao) {

                case INCLUSAO:
                    sql = "INSERT INTO funcao (nome, ativo) VALUES (?, ?)";
                    statement = bd.getConnection().prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
                    statement.setString(1, funcao.getNome());
                    statement.setBoolean(2, funcao.isAtivo());
                    statement.executeUpdate();

                    resultSet = statement.getGeneratedKeys();
                    if (resultSet.next()) {
                        funcao.setId(resultSet.getInt(1));
                    }
                    salvarPermissoes(funcao, bd);
                    return "Função incluída com sucesso.";

                case ALTERACAO:
                    sql = "UPDATE funcao SET nome = ?, ativo = ? WHERE id_funcao = ?";
                    statement = bd.getConnection().prepareStatement(sql);
                    statement.setString(1, funcao.getNome());
                    statement.setBoolean(2, funcao.isAtivo());
                    statement.setInt(3, funcao.getId());
                    statement.executeUpdate();

                    // Recria as permissões do zero
                    deletarPermissoes(funcao.getId(), bd);
                    salvarPermissoes(funcao, bd);
                    return "Função alterada com sucesso.";

                case EXCLUSAO:
                    deletarPermissoes(funcao.getId(), bd);

                    sql = "DELETE FROM funcao WHERE id_funcao = ?";
                    statement = bd.getConnection().prepareStatement(sql);
                    statement.setInt(1, funcao.getId());
                    statement.executeUpdate();
                    return "Função excluída com sucesso.";

                default:
                    return "Operação desconhecida.";
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar Funcao: " + erro.getMessage());
            return "Erro: " + erro.getMessage();
        } finally {
            bd.close();
        }
    }

    // Listar todas as funções

    public ArrayList<Funcao> listarTodas() {
        BD bd = new BD();
        ArrayList<Funcao> lista = new ArrayList<>();
        if (!bd.connect()) return lista;

        try {
            sql = "SELECT id_funcao, nome, ativo FROM funcao ORDER BY nome";
            statement = bd.getConnection().prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Funcao f = new Funcao();
                f.setId(resultSet.getInt("id_funcao"));
                f.setNome(resultSet.getString("nome"));
                f.setAtivo(resultSet.getBoolean("ativo"));
                f.setPermissoes(buscarPermissoes(f.getId(), bd));
                lista.add(f);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar funções: " + erro.getMessage());
        } finally {
            bd.close();
        }

        return lista;
    }

    // Métodos auxiliares de permissão 

    private ArrayList<Permissao> buscarPermissoes(int idFuncao, BD bd) throws SQLException {
        ArrayList<Permissao> permissoes = new ArrayList<>();
        String sqlPerm = "SELECT permissao FROM funcao_permissao WHERE id_funcao = ?";
        PreparedStatement stPerm = bd.getConnection().prepareStatement(sqlPerm);
        stPerm.setInt(1, idFuncao);
        ResultSet rsPerm = stPerm.executeQuery();

        while (rsPerm.next()) {
            try {
                Permissao p = Permissao.valueOf(rsPerm.getString("permissao"));
                permissoes.add(p);
            } catch (IllegalArgumentException e) {
                System.out.println("Permissão desconhecida ignorada: " + rsPerm.getString("permissao"));
            }
        }
        return permissoes;
    }

    private void salvarPermissoes(Funcao f, BD bd) throws SQLException {
        String sqlPerm = "INSERT INTO funcao_permissao (id_funcao, permissao) VALUES (?, ?)";
        PreparedStatement stPerm = bd.getConnection().prepareStatement(sqlPerm);
        for (Permissao p : f.getPermissoes()) {
            stPerm.setInt(1, f.getId());
            stPerm.setString(2, p.name());
            stPerm.executeUpdate();
        }
    }

    private void deletarPermissoes(int idFuncao, BD bd) throws SQLException {
        String sqlDel = "DELETE FROM funcao_permissao WHERE id_funcao = ?";
        PreparedStatement stDel = bd.getConnection().prepareStatement(sqlDel);
        stDel.setInt(1, idFuncao);
        stDel.executeUpdate();
    }
}