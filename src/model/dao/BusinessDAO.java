package model.dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class BusinessDAO {

    private Connection conn = null;

    public BusinessDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    public boolean businessExists(String businessName) throws SQLException {
        String sql = "SELECT 1 FROM empresas WHERE LOWER(nombre) = LOWER(?) LIMIT 1";
        try (PreparedStatement stmt = this.conn.prepareStatement(sql)) {
            stmt.setString(1, businessName);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean insertBusiness(String businessName, double percentage) throws SQLException {
        String sql = "INSERT INTO empresas(nombre, porcentaje) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, businessName);
            ps.setDouble(2, percentage);
            return ps.executeUpdate() > 0;
        }
    }

    public ResultSet listBusinesses() {
        String query = "SELECT id_empresa, nombre, porcentaje FROM empresas";
        try {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(query);
            return ps.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteBusiness(String str) throws SQLException {
        int id = Integer.parseInt(str);
        String sql = "DELETE FROM empresas WHERE id_empresa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Ha ocurrido un error");
        }
        return false;
    }

}
