/*
 * SBDNO - Small Business Delivery Note Organizer
 * 
 * Copyright (C) 2025 Adrián González Hermida
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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

    public boolean insertDeliveryNote(String date,                              //Inserts a delivery note
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

    public boolean deleteDeliveryNote(int deliveryNoteId) {                     //Deletes a delivery note
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

    public boolean editDeliveryNote(                                            //Edits a delivery note
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

    public ResultSet listDeliveryNotes() {                                      //Lists all delivery notes
        String query = "SELECT * FROM delivery_notes";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet listDeliveryNotesByBusinessIdAndDeliveryDate(String deliveryFrom, String deliveryTo, int business_id) {        //Lists delivery notes by business id and delivery date
        String query = "SELECT * FROM delivery_notes WHERE business_id = ? AND delivery_date BETWEEN ? AND ? ORDER BY delivery_date;";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, business_id);
            ps.setString(2, deliveryFrom);
            ps.setString(3, deliveryTo);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet listDeliveryNotesByDeliveryDate(String deliveryDate) {     //Lists delivery notes by delivery date
        String query = "SELECT * FROM delivery_notes WHERE delivery_date = ? ORDER BY truck_id, store_id;";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, deliveryDate);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet filterDeliveryNotes(                                       //Filters delivery notes by the introduced data
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
