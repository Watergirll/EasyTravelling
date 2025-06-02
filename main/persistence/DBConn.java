package main.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn {

    private static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@localhost:1521";
    private static final String USER = "AGENTIE_TURISM";  // utilizatorul tau
    private static final String PASSWORD = "oracle";      // parola nou setata

    private static DBConn instance;
    private Connection connection;

    // Metoda publica de acces la conexiune
    public static Connection getConnectionFromInstance() {
        if (instance == null) {
            instance = new DBConn();
        }
        return instance.connection;
    }

    // Constructor privat ? Singleton
    private DBConn() {
        try {
            // Optional: load driver manual (uneori nu e nevoie din Java 11+)
            // Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✔ Conectat cu succes la baza de date.");
        } catch (SQLException e) {
            System.err.println("❌ Eroare la conectarea cu baza de date: " + e.getMessage());
        }
    }
}

