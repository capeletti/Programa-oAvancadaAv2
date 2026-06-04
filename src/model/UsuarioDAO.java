package model;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAO {
	private BD bd;
	
	private PreparedStatement statement;
	private ResultSet resultSet;
	
	private String sql;
	
	public UsuarioDAO() {
		bd = null;
	}

	public void setBd(BD bd) {
		this.bd = bd;
	}
	
	public Usuario localizar(int id) {

	    sql = "SELECT * FROM usuario WHERE id = ?";

	    try {

	        statement = bd.connection.prepareStatement(sql);
	        statement.setInt(1, id);

	        resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	        	
	        	Funcao funcao = new Funcao();
	        	funcao.setId(resultSet.getInt("id_funcao"));

	        	FuncaoDAO funcaoDAO = new FuncaoDAO(funcao);
	        	funcaoDAO.localizar();

				Usuario usuario = new Usuario(
					    resultSet.getInt("id"),
					    resultSet.getString("nome"),
					    resultSet.getString("email"),
					    resultSet.getString("senha"),
					    resultSet.getDate("data_cadastro"),
					    Setor.valueOf(resultSet.getString("setor")),
					    funcao
				);
	            return usuario;
	        }

	    } catch (SQLException erro) {
	        erro.printStackTrace();
	    }

	    return null;
	}
	
	public ArrayList<Usuario> listar() {
	    ArrayList<Usuario> lista = new ArrayList<>();
	    sql = "SELECT * FROM usuario";

	    try {

	        statement = bd.connection.prepareStatement(sql);

	        resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	        	Funcao funcao = new Funcao();
		        funcao.setId(resultSet.getInt("id_funcao"));
	
		        FuncaoDAO funcaoDAO = new FuncaoDAO(funcao);
		        funcaoDAO.localizar();

		        Usuario usuario = new Usuario(
		        	    resultSet.getInt("id"),
		        	    resultSet.getString("nome"),
		        	    resultSet.getString("email"),
		        	    resultSet.getString("senha"),
		        	    resultSet.getDate("data_cadastro"),
		        	    Setor.valueOf(resultSet.getString("setor")),
		        	    funcao
		        );

	            lista.add(usuario);
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

	            statement = bd.connection.prepareStatement(sql);

	            statement.setString(1, usuario.getNome());
	            statement.setString(2, usuario.getEmail());
	            statement.setString(3, usuario.getSenhaHash());
	            statement.setDate(4, new java.sql.Date(usuario.getDataCadastro().getTime()));
	            statement.setString(5, usuario.getSetor().name());
	            statement.setInt(6, usuario.getFuncao().getId());
	        }
	        else if (operacao == TipoOperacaoBD.ALTERACAO) {

	        	sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, setor = ?, id_funcao = ? WHERE id = ?";
	        	
	            statement = bd.connection.prepareStatement(sql);

	            statement.setString(1, usuario.getNome());
	            statement.setString(2, usuario.getEmail());
	            statement.setString(3, usuario.getSenhaHash());
	            statement.setString(4, usuario.getSetor().name());
	            statement.setInt(5, usuario.getFuncao().getId());
	            statement.setInt(6, usuario.getId());
	        }
	        else if (operacao == TipoOperacaoBD.EXCLUSAO) {

	            sql = "DELETE FROM usuario WHERE id = ?";

	            statement = bd.connection.prepareStatement(sql);

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

	    sql = "SELECT * FROM usuario WHERE email = ?";

	    try {

	        statement = bd.connection.prepareStatement(sql);
	        statement.setString(1, email);

	        resultSet = statement.executeQuery();

	        if (resultSet.next()) {

	        	Funcao funcao = new Funcao();
	        	funcao.setId(resultSet.getInt("id_funcao"));

	        	FuncaoDAO funcaoDAO = new FuncaoDAO(funcao);
	        	funcaoDAO.localizar();

	        	Usuario usuario = new Usuario(
	        	    resultSet.getInt("id"),
	        	    resultSet.getString("nome"),
	        	    resultSet.getString("email"),
	        	    resultSet.getString("senha"),
	        	    resultSet.getDate("data_cadastro"),
	        	    Setor.valueOf(resultSet.getString("setor")),
	        	    funcao
	        	);

	            String hashDigitado = usuario.string2Hash(senha);

	            if (hashDigitado.equals(usuario.getSenhaHash())) {
	                return usuario;
	            }
	        }

	    } catch (SQLException erro) {
	        erro.printStackTrace();
	    }

	    return null;
	}
	
}