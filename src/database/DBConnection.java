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

    public static Connection getConnection() throws SQLException {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL);
                System.out.println("Connection stablished correclty.");
            } catch (SQLException e) {
                System.err.println("Error connecting to database: " + e.getMessage());
            }
        }
        return conn;
    }

    public void initDB() throws SQLException {
        Connection c = getConnection();
        try {
            String sql = new String(Files.readAllBytes(Paths.get(SCRIPT_PATH)));
            Statement st = c.createStatement();
            st.executeUpdate(sql);
            st.close();
            System.out.println("Script executed correctly, tables created if nonexistent.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
