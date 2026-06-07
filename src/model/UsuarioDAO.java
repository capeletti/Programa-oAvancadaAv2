package model;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAO {
	private BD bd;
	
	private PreparedStatement statement;
	private ResultSet resultSet;
	
	private String sql;
	
	public UsuarioDAO(BD bd) {
		this.bd = bd;
	}
	
	public Usuario localizar(int id) {

		sql = SQL_USUARIO_COM_FUNCAO + " WHERE u.id = ?";

	    try {

	        statement = bd.getConnection().prepareStatement(sql);
	        statement.setInt(1, id);

	        resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            return carregarUsuario(resultSet);
	        }

	    } catch (SQLException erro) {
	        erro.printStackTrace();
	    }

	    return null;
	}
	
	public ArrayList<Usuario> listar() {
	    ArrayList<Usuario> lista = new ArrayList<>();
	    sql = SQL_USUARIO_COM_FUNCAO;

	    try {

	        statement = bd.getConnection().prepareStatement(sql);

	        resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            lista.add(carregarUsuario(resultSet));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return lista;
	}
		
	public String atualizar(Usuario usuario, TipoOperacaoBD operacao) {

	    String msg = "Operação realizada com sucesso!";

	    try {

	        if (operacao == TipoOperacaoBD.INCLUSAO) {

	        	sql = "INSERT INTO usuario (nome, email, senha, data_cadastro, setor, id_funcao) VALUES (?, ?, ?, ?, ?, ?)";

	            statement = bd.getConnection().prepareStatement(sql);

	            statement.setString(1, usuario.getNome());
	            statement.setString(2, usuario.getEmail());
	            statement.setString(3, usuario.getSenhaHash());
	            statement.setDate(4, new java.sql.Date(usuario.getDataCadastro().getTime()));
	            statement.setString(5, usuario.getSetor().name());
	            if (usuario.getFuncao() != null) {
	                statement.setInt(6, usuario.getFuncao().getId());
	            } else {
	                statement.setNull(6, Types.INTEGER);
	            }
	        }
	        else if (operacao == TipoOperacaoBD.ALTERACAO) {

	            if (usuario.possuiSenha()) {

	            	sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, setor = ?, id_funcao = ? WHERE id = ?";

	                statement = bd.getConnection().prepareStatement(sql);

	                statement.setString(1, usuario.getNome());
	                statement.setString(2, usuario.getEmail());
	                statement.setString(3, usuario.getSenhaHash());
	                statement.setString(4, usuario.getSetor().name());

	                if (usuario.getFuncao() != null) {
	                    statement.setInt(5, usuario.getFuncao().getId());
	                } else {
	                    statement.setNull(5, Types.INTEGER);
	                }

	                statement.setInt(6, usuario.getId());

	            } else {

	                sql = "UPDATE usuario SET nome = ?, email = ?, setor = ?, id_funcao = ? WHERE id = ?";

	                statement = bd.getConnection().prepareStatement(sql);

	                statement.setString(1, usuario.getNome());
	                statement.setString(2, usuario.getEmail());
	                statement.setString(3, usuario.getSetor().name());

	                if (usuario.getFuncao() != null) {
	                    statement.setInt(4, usuario.getFuncao().getId());
	                } else {
	                    statement.setNull(4, Types.INTEGER);
	                }

	                statement.setInt(5, usuario.getId());
	            }
	        }
	        else if (operacao == TipoOperacaoBD.EXCLUSAO) {

	            sql = "DELETE FROM usuario WHERE id = ?";

	            statement = bd.getConnection().prepareStatement(sql);

	            statement.setInt(1, usuario.getId());
	        }

	        if (statement.executeUpdate() == 0) {
	            msg = "Falha na operação!";
	        }

	    } catch (SQLException erro) {
	        msg = "Falha na operação - " + erro.getMessage();
	    }

	    return msg;
	}
	
	public Usuario validarLogin(String email, String senha) {

		sql = SQL_USUARIO_COM_FUNCAO + " WHERE u.email = ?";

	    try {

	        statement = bd.getConnection().prepareStatement(sql);
	        statement.setString(1, email);

	        resultSet = statement.executeQuery();

	        if (resultSet.next()) {

	            Usuario usuario = carregarUsuario(resultSet);

	            String hashDigitado = Usuario.string2Hash(senha);

	            if (hashDigitado.equals(usuario.getSenhaHash())) {
	                return usuario;
	            }
	        }

	    } catch (SQLException erro) {
	        erro.printStackTrace();
	    }

	    return null;
	}
	
	private Usuario carregarUsuario(ResultSet rs) throws SQLException {

	    Funcao funcao = carregarFuncao(rs);

	    return new Usuario(
	        rs.getInt("id"),
	        rs.getString("nome"),
	        rs.getString("email"),
	        rs.getString("senha"),
	        rs.getDate("data_cadastro"),
	        Setor.valueOf(rs.getString("setor")),
	        funcao
	    );
	}
	
	private Funcao carregarFuncao(ResultSet rs) throws SQLException {
		int idFuncao = rs.getInt("id_funcao");

		if (rs.wasNull()) {
			return null;
		}
	
		Funcao funcao = new Funcao();

		funcao.setId(idFuncao);
		funcao.setNome(rs.getString("funcao_nome"));
		
		return funcao;
	}
	
	private static final String SQL_USUARIO_COM_FUNCAO = """
		    SELECT
		        u.*,
		        f.id_funcao AS funcao_id,
		        f.nome AS funcao_nome
		    FROM usuario u
		    LEFT JOIN funcao f ON f.id_funcao = u.id_funcao
		""";
}