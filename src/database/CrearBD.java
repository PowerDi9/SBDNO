package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CrearBD {
    
    private static final String DB_URL = "jdbc:sqlite:src/database/database.db";
    private static final String SCRIPT_PATH = "src/database/script.sql";
    
    public void CrearBD(){
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("Conexión establecida a SQLite.");

                String sql = new String(Files.readAllBytes(Paths.get(SCRIPT_PATH)));

                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(sql);
                    System.out.println("Script ejecutado correctamente, tablas creadas si no existían.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ha habido un error");
        }
    }
    
}
