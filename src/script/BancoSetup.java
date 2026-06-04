package script;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import model.Setor;
import model.Usuario;

public class BancoSetup {
    
    private Connection connection;

    public BancoSetup(Connection connection) {
        this.connection = connection;
    }

    public void inicializar() {

        try {

            criarTabelas();
            inserirRegistroUsuario();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void criarTabelas() throws SQLException {

    	String sql = """
    		    CREATE TABLE IF NOT EXISTS usuario (
    		        id INTEGER PRIMARY KEY AUTO_INCREMENT,
    		        nome VARCHAR(100) NOT NULL,
    		        email VARCHAR(150) NOT NULL UNIQUE,
    		        senha VARCHAR(255) NOT NULL,
    		        data_cadastro DATE NOT NULL,
    		        setor VARCHAR(50) NOT NULL,
    		        id_funcao INTEGER NULL
    		    )
    	""";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.execute();
        statement.close();
    }

    private void inserirRegistroUsuario() throws SQLException {

        String sqlVerifica = "SELECT COUNT(*) FROM usuario WHERE email = ?";

        PreparedStatement statement = connection.prepareStatement(sqlVerifica);

        statement.setString(1, "admin@ticket.com");

        ResultSet rs = statement.executeQuery();

        rs.next();

        if (rs.getInt(1) == 0) {

        	Usuario admin = new Usuario(
        		    0,
        		    "Administrador",
        		    "admin@ticket.com",
        		    "",
        		    new Date(),
        		    Setor.ADMINISTRACAO,
        		    null
        	);

            admin.setSenhaHash("admin");

            String sqlInsert = "INSERT INTO usuario (nome, email, senha, data_cadastro, setor, id_funcao) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement insert = connection.prepareStatement(sqlInsert);

            insert.setString(1, admin.getNome());
            insert.setString(2, admin.getEmail());
            insert.setString(3, admin.getSenhaHash());
            insert.setDate(4, new java.sql.Date(admin.getDataCadastro().getTime()));
            insert.setString(5, admin.getSetor().name());
            insert.setNull(6, java.sql.Types.INTEGER);

            insert.executeUpdate();
            insert.close();
        }

        rs.close();
        statement.close();
    }
}