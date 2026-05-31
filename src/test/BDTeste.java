package test;

import java.sql.SQLException;

import model.BD;

public class BDTeste {

    public static void main(String[] args) throws SQLException {

        BD bd = new BD();

        if (bd.connect()) {
            System.out.println("Conectado com sucesso!");

            try {
                System.out.println(
                    bd.getConnection().getMetaData().getDatabaseProductName()
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }

            bd.close();
        }
        else {
            System.out.println("Falha na conexão.");
        }
    }
}