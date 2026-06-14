package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ArquivoTicketDAO implements OperacaoBD {

	private ArquivoTicket arquivoTicket;
	private PreparedStatement statement;
	private ResultSet resultSet;
	private String sql;

	public ArquivoTicketDAO(ArquivoTicket arquivoTicket) {
		this.arquivoTicket = arquivoTicket;
	}

	@Override
	public boolean localizar() {

		BD bd = new BD();

		if (!bd.connect()) {
			return false;
		}

		try {

			sql = "SELECT * FROM arquivo_ticket WHERE id = ?";
			statement = bd.getConnection().prepareStatement(sql);
			statement.setInt(1, arquivoTicket.getId());
			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				carregarArquivoTicket(arquivoTicket, resultSet);
				return true;
			}

			return false;

		} catch (SQLException erro) {

			System.out.println("Erro ao localizar Arquivo: " + erro.getMessage());
			return false;

		} finally {
			bd.close();
		}
	}

	@Override
	public String atualizar(TipoOperacaoBD operacao) {

		BD bd = new BD();

		if (!bd.connect()) {
			return "Erro: falha ao conectar ao banco de dados.";
		}

		try {

			switch (operacao) {
			case INCLUSAO:

				sql = "INSERT INTO arquivo_ticket "
					+ "(nome_arquivo, tipo_arquivo, arquivo, data_envio, id_ticket, id_enviado_por) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
				statement = bd.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				prepararCampos();
				statement.executeUpdate();
				resultSet = statement.getGeneratedKeys();

				if (resultSet.next()) {
					arquivoTicket.setId(resultSet.getInt(1));
				}

				return "Arquivo incluido com sucesso.";

			case ALTERACAO:

				sql = "UPDATE arquivo_ticket SET "
					+ "nome_arquivo=?, tipo_arquivo=?, arquivo=?, data_envio=?, "
					+ "id_ticket=?, id_enviado_por=? "
					+ "WHERE id=?";
				statement = bd.getConnection().prepareStatement(sql);
				prepararCampos();
				statement.setInt(7, arquivoTicket.getId());
				statement.executeUpdate();
				return "Arquivo alterado com sucesso.";

			case EXCLUSAO:

				sql = "DELETE FROM arquivo_ticket WHERE id=?";
				statement = bd.getConnection().prepareStatement(sql);
				statement.setInt(1, arquivoTicket.getId());
				statement.executeUpdate();
				return "Arquivo excluido com sucesso.";
				
			default:
				return "Operacao desconhecida.";
			}
		} catch(SQLException erro) {
			System.out.println("Erro ao atualizar Arquivo: " + erro.getMessage());
			return "Erro: " + erro.getMessage();

		} finally {
			bd.close();
		}
	}

	public ArrayList<ArquivoTicket> listarPorTicket(Ticket ticket) {

		ArrayList<ArquivoTicket> lista = new ArrayList<>();
		BD bd = new BD();

		if (!bd.connect()) {
			return lista;
		}

		try {

			sql = "SELECT * FROM arquivo_ticket WHERE id_ticket = ?";

			statement = bd.getConnection().prepareStatement(sql);
			statement.setInt(1, ticket.getId());
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				ArquivoTicket arquivo = new ArquivoTicket();
				carregarArquivoTicket(arquivo, resultSet);
				lista.add(arquivo);
			}
			
		} catch(SQLException erro) {
			System.out.println("Erro ao listar arquivos: " + erro.getMessage());
		} finally {
			bd.close();
		}
		return lista;
	}
	
	private void carregarArquivoTicket(ArquivoTicket arquivo, ResultSet rs) throws SQLException {
		arquivo.setId(rs.getInt("id"));
		arquivo.setNomeArquivo(rs.getString("nome_arquivo"));
		arquivo.setTipoArquivo(rs.getString("tipo_arquivo"));
		arquivo.setArquivo(rs.getBytes("arquivo"));
		arquivo.setDataEnvio(rs.getDate("data_envio"));
		arquivo.setTicket(new Ticket());
		arquivo.getTicket().setId(rs.getInt("id_ticket"));
		arquivo.setEnviadoPor(new Usuario());
		arquivo.getEnviadoPor().setId(rs.getInt("id_enviado_por"));
	}
	
	private void prepararCampos() throws SQLException {
		statement.setString(1, arquivoTicket.getNomeArquivo());
		statement.setString(2, arquivoTicket.getTipoArquivo());
		statement.setBytes(3, arquivoTicket.getArquivo());
		statement.setDate(4, new java.sql.Date(arquivoTicket.getDataEnvio().getTime()));
		statement.setInt(5, arquivoTicket.getTicket().getId());
		statement.setInt(6, arquivoTicket.getEnviadoPor().getId());
	}
}