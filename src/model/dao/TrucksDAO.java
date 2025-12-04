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

public class TrucksDAO {
    private Connection conn = null;

    public TrucksDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    public boolean truckExists(String truckName) throws SQLException {          //Gets the existance of a truck
        String sql = "SELECT 1 FROM trucks WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
            ps.setString(1, truckName);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public boolean insertTruck(String truckName, String description) throws SQLException {  //inserts a truck
        String sql = "INSERT INTO trucks(name, description) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, truckName);
            ps.setString(2, description);
            return ps.executeUpdate() > 0;
        }
    }

    public ResultSet listTrucks() {                                             //Lists all trucks
        String query = "SELECT * FROM trucks";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet listTrucksIdName() {                                       //Lists truck name and id
        String query = "SELECT truck_id, name FROM trucks";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet getTruckName(int truckId) {                                //Gets a truck name
        String query = "SELECT name FROM trucks WHERE truck_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, truckId);
            return ps.executeQuery();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean deleteTrucks(String str) throws SQLException {               //Deletes a truck
        int id = Integer.parseInt(str);
        String sql = "DELETE FROM trucks WHERE truck_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("An error ocurred");
        }
        return false;
    }

    public boolean editTruck(String newId, String name, String description) throws SQLException{        //Edits the desired truck
        int id = Integer.parseInt(newId);
        String sql = "UPDATE trucks SET name = ?, description = ? WHERE truck_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, name);
        ps.setString(2, description);
        ps.setInt(3, id);

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    }

}
