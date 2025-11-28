package model.dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TruckEmployeeIntermediateDAO {

    private Connection conn = null;

    public TruckEmployeeIntermediateDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    public boolean assignEmployeeToTruck(int truckId, int employeeId) {
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

    public boolean removeEmployeeFromTruck(int truckId, int employeeId) {
        String sql = "DELETE FROM truck_employees WHERE truck_id = ? AND employee_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, truckId);
            ps.setInt(2, employeeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet getEmployeesByTruck(int truckId) {
        String sql = "SELECT e.employee_id, e.nameFROM truck_employees te JOIN employees e ON te.employee_id = e.employee_id WHERE te.truck_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, truckId);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet getTruckByEmployee(int employeeId) {
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
