package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DBConnection {

    private static final String DB_URL = "jdbc:sqlite:src/database/database.db";
    private static final String SCRIPT_PATH = "src/database/script.sql";
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL);
                System.out.println("Conexión establecida correctamente.");
            } catch (SQLException e) {
                System.err.println("Error conectando a la base de datos: " + e.getMessage());
            }
        }
        return conn;
    }

    public void initDB() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                String sql = new String(Files.readAllBytes(Paths.get(SCRIPT_PATH)));
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                System.out.println("Script ejecutado correctamente, tablas creadas si no existian.");

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ha habido un error");
        }
    }

}
