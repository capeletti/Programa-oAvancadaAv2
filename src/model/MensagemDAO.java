package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class MensagemDAO implements OperacaoBD {

    private Mensagem mensagem;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private String sql;

    public MensagemDAO(Mensagem mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public boolean localizar() {
        BD bd = new BD();
        if (!bd.connect()) return false;

        try {
            sql = "SELECT id, id_ticket, id_autor, texto, data_envio FROM mensagem WHERE id = ?";
            statement = bd.getConnection().prepareStatement(sql);
            statement.setInt(1, mensagem.getId());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                mensagem.setId(resultSet.getInt("id"));
                mensagem.setTexto(resultSet.getString("texto"));
                mensagem.setDataEnvio(resultSet.getTimestamp("data_envio").toLocalDateTime());

                Ticket ticket = new Ticket();
                ticket.setId(resultSet.getInt("id_ticket"));
                mensagem.setTicket(ticket);

                Usuario autor = new Usuario();
                autor.setId(resultSet.getInt("id_autor"));
                
                mensagem.setAutor(autor);

                return true;
            }
            return false;

        } catch (SQLException erro) {
            System.out.println("Erro ao localizar Mensagem: " + erro.getMessage());
            return false;
        } finally {
            bd.close();
        }
    }

    public boolean localizar(int id) {
        mensagem.setId(id);
        return localizar();
    }

    @Override
    public String atualizar(TipoOperacaoBD operacao) {
        BD bd = new BD();
        if (!bd.connect()) return "Erro: falha ao conectar ao banco de dados.";

        try {
            switch (operacao) {

                case INCLUSAO:
                    sql = "INSERT INTO mensagem (id_ticket, id_autor, texto, data_envio) VALUES (?, ?, ?, ?)";
                    statement = bd.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, mensagem.getTicket().getId());
                    statement.setInt(2, mensagem.getAutor().getId());
                    statement.setString(3, mensagem.getTexto());
                    statement.setTimestamp(4, Timestamp.valueOf(mensagem.getDataEnvio()));
                    statement.executeUpdate();

                    resultSet = statement.getGeneratedKeys();
                    if (resultSet.next()) {
                        mensagem.setId(resultSet.getInt(1));
                    }
                    return "Mensagem enviada com sucesso.";

                case ALTERACAO:
                    sql = "UPDATE mensagem SET texto = ? WHERE id = ?";
                    statement = bd.getConnection().prepareStatement(sql);
                    statement.setString(1, mensagem.getTexto());
                    statement.setInt(2, mensagem.getId());
                    statement.executeUpdate();
                    return "Mensagem alterada com sucesso.";

                case EXCLUSAO:
                    sql = "DELETE FROM mensagem WHERE id = ?";
                    statement = bd.getConnection().prepareStatement(sql);
                    statement.setInt(1, mensagem.getId());
                    statement.executeUpdate();
                    return "Mensagem excluída com sucesso.";

                default:
                    return "Operação desconhecida.";
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar Mensagem: " + erro.getMessage());
            return "Erro: " + erro.getMessage();
        } finally {
            bd.close();
        }
    }

    public ArrayList<Mensagem> listarPorTicket(Ticket ticket) {
        BD bd = new BD();
        ArrayList<Mensagem> lista = new ArrayList<>();
        if (!bd.connect()) return lista;

        try {
            sql = "SELECT m.id, m.texto, m.data_envio, m.id_autor, u.nome AS autor_nome " +
                  "FROM mensagem m " +
                  "JOIN usuario u ON u.id = m.id_autor " +
                  "WHERE m.id_ticket = ? " +
                  "ORDER BY m.data_envio ASC";
            statement = bd.getConnection().prepareStatement(sql);
            statement.setInt(1, ticket.getId());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Mensagem m = new Mensagem();
                m.setId(resultSet.getInt("id"));
                m.setTexto(resultSet.getString("texto"));
                m.setDataEnvio(resultSet.getTimestamp("data_envio").toLocalDateTime());
                m.setTicket(ticket);

                Usuario autor = new Usuario();
                autor.setId(resultSet.getInt("id_autor"));
                autor.setNome(resultSet.getString("autor_nome"));
                m.setAutor(autor);

                lista.add(m);
            }

        } catch (SQLException erro) {
            System.out.println("Erro ao listar mensagens: " + erro.getMessage());
        } finally {
            bd.close();
        }

        return lista;
    }
}