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
        String sql = "SELECT 1 FROM business WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
            ps.setString(1, businessName);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public boolean insertBusiness(String businessName, double percentage) throws SQLException {
        String sql = "INSERT INTO business(name, percentage) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, businessName);
            ps.setDouble(2, percentage);
            return ps.executeUpdate() > 0;
        }
    }

    public ResultSet listBusinesses() {
        String query = "SELECT business_id, name, percentage FROM business";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet listBusinessesIdName(){
        String query = "SELECT business_id, name FROM business";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteBusiness(String str) throws SQLException {
        int id = Integer.parseInt(str);
        String sql = "DELETE FROM business WHERE business_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("An error ocurred");
        }
        return false;
    }

    public boolean editBusiness(String newId, String name, double percentage) throws SQLException{
        int id = Integer.parseInt(newId);
        String sql = "UPDATE business SET name = ?, percentage = ? WHERE business_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, name);
        ps.setDouble(2, percentage);
        ps.setInt(3, id);

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    }

}
