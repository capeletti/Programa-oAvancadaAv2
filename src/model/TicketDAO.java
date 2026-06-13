package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
				carregarTicket(ticket, resultSet);
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

	public ArrayList<Ticket> listarTodos() {
		ArrayList<Ticket> lista = new ArrayList<>();
		BD bd = new BD();
		if (!bd.connect()) return lista;

		try {
			sql = "SELECT * FROM ticket ORDER BY data_abertura DESC";
			statement = bd.getConnection().prepareStatement(sql);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Ticket t = new Ticket();
				carregarTicket(t, resultSet);
				lista.add(t);
			}

		} catch (SQLException erro) {
			System.out.println("Erro ao listar Tickets: " + erro.getMessage());
		} finally {
			bd.close();
		}

		return lista;
	}

	public ArrayList<Ticket> listarPorSetor(Setor setor) {
		ArrayList<Ticket> lista = new ArrayList<>();
		BD bd = new BD();
		if (!bd.connect()) return lista;

		try {
			sql = "SELECT * FROM ticket WHERE setor_destino = ? "
			    + "ORDER BY FIELD(status, 'ABERTO', 'EM_ANDAMENTO', 'FECHADO'), "
			    + "FIELD(prioridade, 'URGENTE', 'ALTA', 'MEDIA', 'BAIXA'), data_abertura ASC";
			statement = bd.getConnection().prepareStatement(sql);
			statement.setString(1, setor.name());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Ticket t = new Ticket();
				carregarTicket(t, resultSet);
				lista.add(t);
			}

		} catch (SQLException erro) {
			System.out.println("Erro ao listar Tickets por setor: " + erro.getMessage());
		} finally {
			bd.close();
		}

		return lista;
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
				carregarTicket(t, resultSet);
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

	private void carregarTicket(Ticket t, ResultSet rs) throws SQLException {
		t.setId(rs.getInt("id"));
		t.setTitulo(rs.getString("titulo"));
		t.setDescricao(rs.getString("descricao"));
		t.setSetorDestino(Setor.valueOf(rs.getString("setor_destino")));
		t.setStatus(Status.valueOf(rs.getString("status")));
		t.setPrioridade(Prioridade.valueOf(rs.getString("prioridade")));
		t.setCategoria(Categoria.valueOf(rs.getString("categoria")));
		t.setDataAbertura(rs.getDate("data_abertura"));
		t.setDataFechamento(rs.getDate("data_fechamento"));

		t.setCriadoPor(new Usuario(
			rs.getInt("id_criado_por"),
			null, null, null, null, null, null
		));

		int idRespondido = rs.getInt("id_respondido_por");
		if (!rs.wasNull()) {
			t.setRespondidoPor(new Usuario(
				idRespondido,
				null, null, null, null, null, null
			));
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
}
