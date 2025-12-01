package model.dao;

import database.DBConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientsDAO {

    private Connection conn = null;

    public ClientsDAO() throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    public boolean clientExists(String clientName) throws SQLException {
        String sql = "SELECT 1 FROM clients WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clientName);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public boolean insertClient(String clientName, String phoneNumber) throws SQLException {
        String sql = "INSERT INTO clients(name, phone) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clientName);
            ps.setString(2, phoneNumber);
            return ps.executeUpdate() > 0;
        }
    }
    
    public int returnGeneratedKeyInsertClient(String clientName, String phoneNumber) throws SQLException {
        String sql = "INSERT INTO clients(name, phone) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, clientName);
            ps.setString(2, phoneNumber);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            int id = rs.getInt(1);
            return id;
        }
    }
    
    public ResultSet getClientNamePhone(int clientId) {
        String query = "SELECT name, phone FROM clients WHERE client_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, clientId);
            return ps.executeQuery();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ResultSet getClientName(int clientId) {
        String query = "SELECT name FROM clients WHERE client_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, clientId);
            return ps.executeQuery();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet listClients() {
        String query = "SELECT * FROM clients";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet searchClients(String name, String phone) {
        String query = "SELECT * FROM clients WHERE 1=1";
        ArrayList<Object> parameters = new ArrayList<>();
        try {
            if (name != null && !name.isEmpty() && !name.isBlank()) {
                query += " AND name LIKE ?";
                parameters.add(name);
            }if(phone != null && !phone.isEmpty() && !phone.isBlank()){
                query += " AND phone LIKE ?";
                parameters.add(phone);
            }
            PreparedStatement ps = conn.prepareStatement(query);
            for(int i = 0; i < parameters.size(); i++){
                ps.setObject(i + 1, parameters.get(i));
            }
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteClient(String str) throws SQLException {
        int id = Integer.parseInt(str);
        String sql = "DELETE FROM clients WHERE client_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error ocurred");
        }
        return false;
    }

    public boolean editClient(String newId, String name, String phoneNumber) throws SQLException {
        int id = Integer.parseInt(newId);
        String sql = "UPDATE clients SET name = ?, phone = ? WHERE client_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phoneNumber);
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
