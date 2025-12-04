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

public class SellersDAO {

    private Connection conn = null;

    public SellersDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    public boolean insertSeller(int storeId, String sellerName) throws SQLException {           //Inserts a seller
        String sql = "INSERT INTO sellers(store_id, name) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            ps.setString(2, sellerName);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteSeller(String str) throws SQLException {               //Deletes a seller
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

    public boolean editSeller(String sellerId, String storeId, String name) throws SQLException {       //Edits a seller
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

    public ResultSet listSellers() {                                            //Lists all sellers
        String query = "SELECT s.seller_id, s.store_id, st.name AS store_name, s.name AS seller_name FROM sellers s JOIN stores st ON s.store_id = st.store_id ORDER BY s.store_id;";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet listSellersIdNameByStoreId(int storeId){                   //List sellers id and name by store id
        String query = "SELECT s.seller_id, s.name AS seller_name FROM sellers s JOIN stores st ON s.store_id = st.store_id WHERE s.store_id = ? ORDER BY s.seller_id;";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, storeId);
            return ps.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet getSellerName(int sellerId) {                              //Gets a seller name
        String query = "SELECT name FROM sellers WHERE seller_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, sellerId);
            return ps.executeQuery();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean sellerExists(String sellerName) throws SQLException {        //Gets the existance of a seller
        String sql = "SELECT 1 FROM sellers WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
            ps.setString(1, sellerName);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

}
