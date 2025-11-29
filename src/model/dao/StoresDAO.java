package model.dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoresDAO {

    private Connection conn = null;

    public StoresDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    public boolean insertStore(int businessId, String storeName) throws SQLException {
        String sql = "INSERT INTO stores(business_id, name) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, businessId);
            ps.setString(2, storeName);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteStore(String str) throws SQLException {
        int id = Integer.parseInt(str);
        String sql = "DELETE FROM stores WHERE store_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error ocurred");
        }
        return false;
    }

    public boolean editStore(String storeId, String businessId, String name) throws SQLException {
        int id = Integer.parseInt(storeId);
        int bsid = Integer.parseInt(businessId);
        String sql = "UPDATE stores SET name = ?, business_id = ? WHERE store_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, bsid);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet listStores() {
        String query = "SELECT s.store_id, s.business_id, b.name AS business_name, s.name AS store_name FROM stores s JOIN business b ON s.business_id = b.business_id ORDER BY s.business_id;";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet listStoresIdName(){
        String query = "SELECT store_id, name FROM stores";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean storeExists(String storeName) throws SQLException {
        String sql = "SELECT 1 FROM stores WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
            ps.setString(1, storeName);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

}
