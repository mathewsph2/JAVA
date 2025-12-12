/* ---------------------------------------------------------------------------- */
// Conexão ao banco de dados
// 
/* ---------------------------------------------------------------------------- */
package org.matheus.ConnectionFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    public static Connection getConnection() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/sibov",
                    "matheus",
                    "matheus"
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }

    public static void main(String[] args) {
        ConnectionFactory.getConnection();
        System.out.println("Conectado!");

    }
}
