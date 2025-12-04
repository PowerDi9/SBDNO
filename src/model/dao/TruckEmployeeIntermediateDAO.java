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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TruckEmployeeIntermediateDAO {

    private Connection conn = null;

    public TruckEmployeeIntermediateDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    public boolean assignEmployeeToTruck(int truckId, int employeeId) {         //Assigns an employee to a truck
        String sql = "INSERT INTO truck_employees (truck_id, employee_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, truckId);
            ps.setInt(2, employeeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeEmployeeFromTruck(int truckId, int employeeId) {       //Removes an employee from a truck
        String sql = "DELETE FROM truck_employees WHERE truck_id = ? AND employee_id = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, truckId);
            ps.setInt(2, employeeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getEmployeesByTruck(int truckId) {                         //Gets all employees of a truck
        String sql = "SELECT e.employee_id, e.name, e.state FROM truck_employees te JOIN employees e ON te.employee_id = e.employee_id WHERE te.truck_id = ? ORDER BY e.name ASC";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, truckId);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet getTruckByEmployee(int employeeId) {                       //Getrs the truck an employee is assigned to 
        String sql = "SELECT t.truck_id, t.description FROM truck_employees te JOIN trucks t ON te.truck_id = t.truck_id WHERE te.employee_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
