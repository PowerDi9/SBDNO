package model.dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeliveryNoteDAO {
    
    private Connection conn = null;

    public DeliveryNoteDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }
    
    public boolean insertDeliveryNote(String date, String deliveryDate, Double amount, int clientId, int sellerId, int businessId, int storeId, int truckId, String pdfPath) throws SQLException {
        String sql = "INSERT INTO delivery_notes (date, delivery_date, amount, client_id, seller_id, business_id, store_id, truck_id, pdf_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, date);
            ps.setString(2, deliveryDate);
            ps.setDouble(3, amount);
            ps.setInt(4, clientId);
            ps.setInt(5, sellerId);
            ps.setInt(6, businessId);
            ps.setInt(7, storeId);
            ps.setInt(8, truckId);
            ps.setString(9, pdfPath);
            return ps.executeUpdate() > 0;
        }
    }

    
}
