package model.dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SellersDAO {

    private Connection conn = null;

    public SellersDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    public boolean insertSeller(int storeId, String sellerName) throws SQLException {
        String sql = "INSERT INTO sellers(store_id, name) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setString(2, sellerName);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteSeller(String str) throws SQLException {
        int id = Integer.parseInt(str);
        String sql = "DELETE FROM sellers WHERE seller_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error ocurred");
        }
        return false;
    }

    public boolean editSeller(String sellerId, String storeId, String name) throws SQLException {
        int id = Integer.parseInt(sellerId);
        int sid = Integer.parseInt(storeId);
        String sql = "UPDATE sellers SET name = ?, store_id = ? WHERE seller_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, sid);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet listSellers() {
        String query = "SELECT s.seller_id, s.store_id, st.name AS store_name, s.name AS seller_name FROM sellers s JOIN stores st ON s.store_id = st.store_id ORDER BY s.store_id;";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean sellerExists(String sellerName) throws SQLException {
        String sql = "SELECT 1 FROM sellers WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
            ps.setString(1, sellerName);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

}
