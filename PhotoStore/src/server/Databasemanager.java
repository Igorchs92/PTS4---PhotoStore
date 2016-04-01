/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ClientType;

/**
 *
 * @author Jeroen0606
 */
public class Databasemanager {

    Connection conn;

    public Databasemanager() {
        String url = "jdbc:mysql://sql7.freesqldatabase.com:3306/";
        String dbName = "sql7112896";
        String userName = "sql7112896";
        String password = "Md3KSKmmsh";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url + dbName, userName, password);
        } catch (Exception e) {
            System.out.println("Connecting to database failed");
            e.printStackTrace();
        }
    }

    public boolean login(ClientType type, String email, String password) {
        try {
            String sql = "SELECT * FROM '?' WHERE acc_id = '?' AND password = '?' AND status = '1';";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, type.toString());
            ps.setString(2, email);
            ps.setString(3, password);
            ResultSet srs = ps.executeQuery();
            if (srs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean registerUser(String email, String password, String name, String phone, String address, String zipcode, String city, String country) {
        try {
            String sql = "INSERT INTO User(email, password, name, phone, address, zipcode, city, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, phone);            
            ps.setString(5, address);
            ps.setString(6, zipcode);
            ps.setString(7, city);
            ps.setString(8, country);
            ps.setString(9, zipcode);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, null,  ex);
            return false;
        }
    }
    
    public boolean registerPhotographer(String email, String password, String name, String phone, String address, String zipcode, String city, String country, String kvk){
        try {
            String sql = "UPDATE Photographer SET password = ?, name = ?, phone = ?, address = ?, zipcode = ?, city = ?, country = ?, kvk = ?, status = ? WHERE email = ? AND password = ? AND status = '0';";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, password);
            ps.setString(2, name);
            ps.setString(3, phone);            
            ps.setString(4, address);
            ps.setString(5, zipcode);
            ps.setString(6, city);
            ps.setString(7, country);
            ps.setString(8, zipcode);
            ps.setString(9, kvk);
            ps.setString(10, email);
            ps.setString(11, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean registerProducer(String email, String password, String name){
        try {
            String sql = "INSERT INTO Producer(email, password, name) VALUES (?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, null,  ex);
            return false;
        }
    }
}
