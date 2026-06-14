package model;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAO implements OperacaoBD {
	private Usuario usuario;
	private PreparedStatement statement;
	private ResultSet resultSet;
	private String sql;
	
	public UsuarioDAO(Usuario usuario) {
	    this.usuario = usuario;
	}
	
	@Override
	public boolean localizar() {

	    BD bd = new BD();

	    if (!bd.connect()) {
	        return false;
	    }

	    try {
	        sql = SQL_USUARIO_COM_FUNCAO + " WHERE u.id = ?";
	        statement = bd.getConnection().prepareStatement(sql);
	        statement.setInt(1, usuario.getId());
	        resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	            carregarUsuario(usuario, resultSet);
	            return true;
	        }
	        return false;
	        
	    } catch (SQLException erro) {
	        System.out.println("Erro ao localizar Usuario: " + erro.getMessage());
	        return false;
	    } finally {
	        bd.close();
	    }
	}
	
	public ArrayList<Usuario> listar() {

	    ArrayList<Usuario> lista = new ArrayList<>();

	    BD bd = new BD();

	    if (!bd.connect()) {
	        return lista;
	    }

	    try {
	        sql = SQL_USUARIO_COM_FUNCAO;
	        statement = bd.getConnection().prepareStatement(sql);
	        resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            Usuario u = new Usuario();
	            carregarUsuario(u, resultSet);
	            lista.add(u);
	        }

	    } catch (SQLException erro) {
	        System.out.println("Erro ao listar Usuarios: " + erro.getMessage());
	    } finally {
	        bd.close();
	    }
	    return lista;
	}
		
	@Override
	public String atualizar(TipoOperacaoBD operacao) {

	    BD bd = new BD();

	    if (!bd.connect()) {
	        return "Erro: falha ao conectar ao banco.";
	    }

	    try {
	        switch (operacao) {
	            case INCLUSAO:
	            	
	                sql = "INSERT INTO usuario "
	                    + "(nome, email, senha, data_cadastro, setor, id_funcao) "
	                    + "VALUES (?, ?, ?, ?, ?, ?)";
	                statement = bd.getConnection().prepareStatement(sql);
	                prepararCampos();
	                statement.executeUpdate();
	                return "Usuario incluido com sucesso.";

	            case ALTERACAO:
	            	
	                if(usuario.possuiSenha()) {
	                    sql = "UPDATE usuario SET nome=?, email=?, senha=?, setor=?, id_funcao=? WHERE id=?";
	                } else {
	                    sql = "UPDATE usuario SET nome=?, email=?, setor=?, id_funcao=? WHERE id=?";
	                }
	                statement = bd.getConnection().prepareStatement(sql);
	                prepararCampos();
	                statement.setInt(6, usuario.getId());
	                statement.executeUpdate();
	                return "Usuario alterado com sucesso.";

	            case EXCLUSAO:

	                sql = "DELETE FROM usuario WHERE id=?";
	                statement = bd.getConnection().prepareStatement(sql);
	                statement.setInt(1, usuario.getId());
	                statement.executeUpdate();
	                return "Usuario excluido com sucesso.";
	                
	            default:
	                return "Operação desconhecida.";
	        }
	    } catch(SQLException erro) {
	        return "Erro: " + erro.getMessage();
	    } finally {
	        bd.close();
	    }
	}
	
	public Usuario validarLogin(String email, String senha) {

	    BD bd = new BD();
	    if (!bd.connect()) {
	        return null;
	    }

	    try {

	        sql = SQL_USUARIO_COM_FUNCAO + " WHERE u.email = ?";
	        statement = bd.getConnection().prepareStatement(sql);
	        statement.setString(1, email);
	        resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            Usuario usuarioLogin = new Usuario();
	            carregarUsuario(usuarioLogin, resultSet);
	            String hashDigitado = Usuario.string2Hash(senha);
	            if (hashDigitado.equals(usuarioLogin.getSenhaHash())) {
	                return usuarioLogin;
	            }
	        }
	    } catch (SQLException erro) {
	        System.out.println("Erro ao validar login: " + erro.getMessage());
	    } finally {
	        bd.close();
	    }
	    return null;
	}
	
	private void prepararCampos() throws SQLException {

	    statement.setString(1, usuario.getNome());
	    statement.setString(2, usuario.getEmail());
	    statement.setString(3, usuario.getSenhaHash());
	    statement.setDate(4, new java.sql.Date(usuario.getDataCadastro().getTime()));
	    statement.setString(5, usuario.getSetor().name());

	    if (usuario.getFuncao() != null) {
	        statement.setInt(6, usuario.getFuncao().getId() );
	    } else {
	        statement.setNull(6, Types.INTEGER);
	    }
	}
	
	private void carregarUsuario(Usuario u, ResultSet rs) throws SQLException {
	    u.setId(rs.getInt("id"));
	    u.setNome(rs.getString("nome"));
	    u.setEmail(rs.getString("email"));
	    u.setSenhaHash(rs.getString("senha"));
	    u.setDataCadastro(rs.getDate("data_cadastro"));
	    u.setSetor(Setor.valueOf(rs.getString("setor")));
	    u.setFuncao(carregarFuncao(rs));
	}
	
	private Funcao carregarFuncao(ResultSet rs) throws SQLException {

	    int idFuncao = rs.getInt("id_funcao");
	    if (rs.wasNull()) {
	        return null;
	    }
	    Funcao funcao = new Funcao();
	    funcao.setId(idFuncao);
	    funcao.setNome(rs.getString("funcao_nome"));
	    FuncaoDAO funcaoDAO = new FuncaoDAO(funcao);
	    funcaoDAO.localizar();
	    return funcao;
	}
	
	private static final String SQL_USUARIO_COM_FUNCAO = "SELECT u.*, f.id_funcao AS funcao_id, f.nome AS funcao_nome FROM usuario u LEFT JOIN funcao f ON f.id_funcao = u.id_funcao";
}