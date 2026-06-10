package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketDAO implements OperacaoBD {

	private Ticket ticket;
	private PreparedStatement statement;
	private ResultSet resultSet;
	private String sql;


	public TicketDAO(Ticket ticket) {
		this.ticket = ticket;
	}

	
	@Override
	public boolean localizar() {
		BD bd = new BD();
		if (!bd.connect()) return false;

		try {
			sql = "SELECT * FROM ticket WHERE id = ?";
			statement = bd.getConnection().prepareStatement(sql);
			statement.setInt(1, ticket.getId());
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				ticket.setId(resultSet.getInt("id"));
				ticket.setTitulo(resultSet.getString("titulo"));
				ticket.setDescricao(resultSet.getString("descricao"));
				ticket.setSetorDestino(Setor.valueOf(resultSet.getString("setor_destino")));
				ticket.setStatus(Status.valueOf(resultSet.getString("status")));
				ticket.setPrioridade(Prioridade.valueOf(resultSet.getString("prioridade")));
				ticket.setCategoria(Categoria.valueOf(resultSet.getString("categoria")));
				ticket.setDataAbertura(resultSet.getDate("data_abertura"));
				ticket.setDataFechamento(resultSet.getDate("data_fechamento"));

				ticket.setCriadoPor(new Usuario(
					resultSet.getInt("id_criado_por"),
					null, null, null, null, null, null
				));

				int idRespondido = resultSet.getInt("id_respondido_por");
				if (!resultSet.wasNull()) {
					ticket.setRespondidoPor(new Usuario(
						idRespondido,
						null, null, null, null, null, null
					));
				}

				return true;
			}
			return false;

		} catch (SQLException erro) {
			System.out.println("Erro ao localizar Ticket: " + erro.getMessage());
			return false;
		} finally {
			bd.close();
		}
	}

	@Override
	public String atualizar(TipoOperacaoBD operacao) {
		BD bd = new BD();
		if (!bd.connect()) return "Erro: falha ao conectar ao banco de dados.";

		try {
			switch (operacao) {

				case INCLUSAO:
					sql = "INSERT INTO ticket (titulo, descricao, setor_destino, status, prioridade, categoria, data_abertura, data_fechamento, id_criado_por, id_respondido_por) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					statement = bd.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
					prepararCampos();
					statement.executeUpdate();

					resultSet = statement.getGeneratedKeys();
					if (resultSet.next()) {
						ticket.setId(resultSet.getInt(1));
					}
					return "Ticket incluido com sucesso.";

				case ALTERACAO:
					sql = "UPDATE ticket SET titulo = ?, descricao = ?, setor_destino = ?, status = ?, prioridade = ?, categoria = ?, data_abertura = ?, data_fechamento = ?, id_criado_por = ?, id_respondido_por = ? WHERE id = ?";
					statement = bd.getConnection().prepareStatement(sql);
					prepararCampos();
					statement.setInt(11, ticket.getId());
					statement.executeUpdate();
					return "Ticket alterado com sucesso.";

				case EXCLUSAO:
					sql = "DELETE FROM ticket WHERE id = ?";
					statement = bd.getConnection().prepareStatement(sql);
					statement.setInt(1, ticket.getId());
					statement.executeUpdate();
					return "Ticket excluido com sucesso.";

				default:
					return "Operacao desconhecida.";
			}

		} catch (SQLException erro) {
			System.out.println("Erro ao atualizar Ticket: " + erro.getMessage());
			return "Erro: " + erro.getMessage();
		} finally {
			bd.close();
		}
	}

	private void prepararCampos() throws SQLException {
		statement.setString(1, ticket.getTitulo());
		statement.setString(2, ticket.getDescricao());
		statement.setString(3, ticket.getSetorDestino().name());
		statement.setString(4, ticket.getStatus().name());
		statement.setString(5, ticket.getPrioridade().name());
		statement.setString(6, ticket.getCategoria().name());
		statement.setDate(7, new java.sql.Date(ticket.getDataAbertura().getTime()));

		if (ticket.getDataFechamento() != null) {
			statement.setDate(8, new java.sql.Date(ticket.getDataFechamento().getTime()));
		} else {
			statement.setNull(8, java.sql.Types.DATE);
		}

		statement.setInt(9, ticket.getCriadoPor().getId());

		if (ticket.getRespondidoPor() != null) {
			statement.setInt(10, ticket.getRespondidoPor().getId());
		} else {
			statement.setNull(10, java.sql.Types.INTEGER);
		}
	}
	public Ticket proximoDaFila(Setor setor) {
		BD bd = new BD();
		if (!bd.connect()) return null;

		try {
			sql = "SELECT * FROM ticket "
			    + "WHERE setor_destino = ? AND status = ? "
			    + "ORDER BY FIELD(prioridade, 'URGENTE', 'ALTA', 'MEDIA', 'BAIXA'), data_abertura ASC "
			    + "LIMIT 1";
			statement = bd.getConnection().prepareStatement(sql);
			statement.setString(1, setor.name());
			statement.setString(2, Status.ABERTO.name());
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				Ticket t = new Ticket();
				t.setId(resultSet.getInt("id"));
				t.setTitulo(resultSet.getString("titulo"));
				t.setDescricao(resultSet.getString("descricao"));
				t.setSetorDestino(Setor.valueOf(resultSet.getString("setor_destino")));
				t.setStatus(Status.valueOf(resultSet.getString("status")));
				t.setPrioridade(Prioridade.valueOf(resultSet.getString("prioridade")));
				t.setCategoria(Categoria.valueOf(resultSet.getString("categoria")));
				t.setDataAbertura(resultSet.getDate("data_abertura"));
				t.setDataFechamento(resultSet.getDate("data_fechamento"));

				t.setCriadoPor(new Usuario(
					resultSet.getInt("id_criado_por"),
					null, null, null, null, null, null
				));

				int idRespondido = resultSet.getInt("id_respondido_por");
				if (!resultSet.wasNull()) {
					t.setRespondidoPor(new Usuario(
						idRespondido,
						null, null, null, null, null, null
					));
				}

				return t;
			}
			return null;

		} catch (SQLException erro) {
			System.out.println("Erro ao buscar proximo da fila: " + erro.getMessage());
			return null;
		} finally {
			bd.close();
		}
	}

	
}
