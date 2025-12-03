package model.dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeesDAO {
    
    private Connection conn = null;
    
    public EmployeesDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    public boolean insertEmployee(String employeeName, String status) throws SQLException {             //Inserts an employee
        String sql = "INSERT INTO employees(name, status) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employeeName);
            ps.setString(2, status);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteEmployee(String str) throws SQLException {             //Deletes an employee
        int id = Integer.parseInt(str);
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error ocurred");
        }
        return false;
    }

    public boolean editEmployee(String employeeId, String employeeName, String status) throws SQLException {        //Edits an employee
        int id = Integer.parseInt(employeeId);
        String sql = "UPDATE employees SET name = ?, status = ? WHERE employee_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employeeName);
            ps.setString(2, status);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet listEmployees() {                                          //Lists all employees
        String query = "SELECT * FROM employees ORDER BY employee_id;";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean employeeExists(String employeeName) throws SQLException {    //Gets the existance of an employee
        String sql = "SELECT 1 FROM employees WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
            ps.setString(1, employeeName);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

}
