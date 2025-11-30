package model.dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DeliveryNoteDAO {

    private Connection conn = null;

    public DeliveryNoteDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    public boolean insertDeliveryNote(String date, 
            String deliveryDate, 
            Double amount, 
            int clientId, 
            int sellerId, 
            int businessId, 
            int storeId, 
            int truckId, 
            String pdfPath) throws SQLException {
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

    public boolean deleteDeliveryNote(int deliveryNoteId) {
        String sql = "DELETE FROM delivery_notes WHERE delivery_note_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deliveryNoteId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error ocurred");
        }
        return false;
    }

    public boolean editDeliveryNote(
            int newId, 
            String date, 
            String deliveryDate, 
            Double amount, 
            int clientId, 
            int sellerId, 
            int businessId, 
            int storeId, 
            int truckId, 
            String pdfPath) throws SQLException {
        String sql = "UPDATE delivery_notes SET date = ?, delivery_date = ?, amount = ?, client_id = ?, seller_id = ?, business_id = ?, store_id = ?, truck_id = ?, pdf_path = ? WHERE delivery_note_id = ?";
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
            ps.setInt(10, newId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet listDeliveryNotes() {
        String query = "SELECT * FROM delivery_notes";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet filterDeliveryNotes(
            String dateFrom,
            String dateTo,
            String deliveryFrom,
            String deliveryTo,
            String clientName,
            String clientPhone,
            Integer sellerId,
            Integer businessId,
            Integer storeId,
            Integer truckId,
            Double amountMin,
            Double amountMax
    ) throws SQLException {

        StringBuilder sql = new StringBuilder("SELECT dn.* FROM delivery_notes dn JOIN clients c ON dn.client_id = c.client_id WHERE 1=1 ");
        ArrayList<Object> parameters = new ArrayList<>();
        
        if (dateFrom != null) {
            sql.append(" AND dn.date >= ? ");
            parameters.add(dateFrom);
        }
        if (dateTo != null) {
            sql.append(" AND dn.date <= ? ");
            parameters.add(dateTo);
        }
        if (deliveryFrom != null) {
            sql.append(" AND dn.delivery_date >= ? ");
            parameters.add(deliveryFrom);
        }
        if (deliveryTo != null) {
            sql.append(" AND dn.delivery_date <= ? ");
            parameters.add(deliveryTo);
        }
        if (clientName != null && !clientName.isBlank()) {
            sql.append(" AND c.name LIKE ? ");
            parameters.add("%" + clientName + "%");
        }
        if (clientPhone != null && !clientPhone.isBlank()) {
            sql.append(" AND c.phone LIKE ? ");
            parameters.add("%" + clientPhone + "%");
        }
        if (sellerId != null) {
            sql.append(" AND dn.seller_id = ? ");
            parameters.add(sellerId);
        }
        if (businessId != null) {
            sql.append(" AND dn.business_id = ? ");
            parameters.add(businessId);
        }
        if (storeId != null) {
            sql.append(" AND dn.store_id = ? ");
            parameters.add(storeId);
        }
        if (truckId != null) {
            sql.append(" AND dn.truck_id = ? ");
            parameters.add(truckId);
        }
        if (amountMin != null) {
            sql.append(" AND dn.amount >= ? ");
            parameters.add(amountMin);
        }
        if (amountMax != null) {
            sql.append(" AND dn.amount <= ? ");
            parameters.add(amountMax);
        }

        sql.append(" ORDER BY dn.delivery_note_id DESC ");
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        
        for (int i = 0; i < parameters.size(); i++) {
            ps.setObject(i + 1, parameters.get(i));
        }

        return ps.executeQuery();
    }
}
