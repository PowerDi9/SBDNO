package model.dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class BusinessDAO {

    private Connection conn = null;

    public BusinessDAO() throws SQLException {                                  //Gets the connection
        this.conn = DBConnection.getConnection();
    }

    public boolean businessExists(String businessName) throws SQLException {    //Gets the existance of a business
        String sql = "SELECT 1 FROM business WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
            ps.setString(1, businessName);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public boolean insertBusiness(String businessName, double percentage) throws SQLException {     //Inserts a business
        String sql = "INSERT INTO business(name, percentage) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, businessName);
            ps.setDouble(2, percentage);
            return ps.executeUpdate() > 0;
        }
    }

    public ResultSet listBusinesses() {                                         //Lists the businesses
        String query = "SELECT * FROM business";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet listBusinessesIdName(){                                    //Lists the businesses id and name
        String query = "SELECT business_id, name FROM business";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet getBusinessName(int businessId) {                          //Gets the business name by id
        String query = "SELECT name FROM business WHERE business_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, businessId);
            return ps.executeQuery();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet getBusinessPercentage(int businessId) {                    //Gets the business percentage by id
        String query = "SELECT percentage FROM business WHERE business_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, businessId);
            return ps.executeQuery();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteBusiness(String str) throws SQLException {             //Deletes the business by id
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

    public boolean editBusiness(String newId, String name, double percentage) throws SQLException{              //Edits the business 
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
