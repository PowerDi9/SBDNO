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
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DBConnection {                             //Class for initializing the DB and getting the connection

    private static final String DB_URL = "jdbc:sqlite:database/database.db";                //Sets the location of the DB
    private static final String SCRIPT_PATH = "database/script.sql";                        //Sets the location of the creation script of the DB
    private static Connection conn = null;

    public static Connection getConnection() throws SQLException {                          //Singleton architecture for the DB connection
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL);
                System.out.println("Connection stablished correclty.");
            } catch (SQLException e) {
                System.err.println("Error connecting to database: " + e.getMessage());
            }
        }
        return conn;
    }

    public void initDB() throws SQLException {                                              //Initializes the DB
        Connection c = getConnection();
        try {
            String sql = new String(Files.readAllBytes(Paths.get(SCRIPT_PATH)));
            Statement st = c.createStatement();
            st.executeUpdate(sql);
            st.close();
            System.out.println("Script executed correctly, tables created if nonexistent.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
