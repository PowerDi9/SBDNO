package model.dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;



public class BusinessDAO {
    
    private Connection conn;

    public BusinessDAO() {
    }
    
    public boolean businessExists(String businessName) throws SQLException {
        String sql = "SELECT 1 FROM empresas WHERE LOWER(nombre) = LOWER(?) LIMIT 1";
        conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, businessName);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    
    public boolean insertBusiness(String businessName) throws SQLException {
        String sql = "INSERT INTO empresas(nombre) VALUES(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, businessName);
            return stmt.executeUpdate() > 0;
        }
    }
    
}
