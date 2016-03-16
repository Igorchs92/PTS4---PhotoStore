/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import shared.user.Account;

/**
 *
 * @author Jeroen0606
 */
public class Databasemanager {

    Connection conn;

    public Databasemanager() {
        String url = "jdbc:mysql://sql2.freesqldatabase.com:3306/";
        String dbName = "sql294852";
        String userName = "sql294852";
        String password = "dA7%fV6*";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url + dbName, userName, password);
        } catch (Exception e) {
            System.out.println("Connecting to database failed");
            e.printStackTrace();
        }
    }

    public Account inloggen(String username, String password) throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("SELECT id, name, adres, phonenumber, role FROM Accounts WHERE id = LOWER('" + username + "') AND password = '" + password + "';");
        ResultSet srs = st.getResultSet();
        String uname = "";
        String name = "";
        String address = "";
        String phonenumber = "";
        String role = "";

        if (srs != null) {
            while (srs.next()) {
                uname = srs.getString("id");
                name = srs.getString("name");
                address = srs.getString("adres");
                phonenumber = srs.getString("phonenumber");
                role = srs.getString("role");
            }
        }
        
        //To Do
        System.out.println("Inloggen returnt nog niks vanwege foute klassenamen!");
        
        switch (role) {
            case "photographer":
                //return new Photographer(uname, name, address, phonenumber);
            case "user":
                //return new User(uname, name, address, phonenumber);
            case "producer":
                //return new Producer(uname, name, address, phonenumber);
        }
        return null;
    }

    public void registreren(String uname, String name, String password, String address, String phonenumber, String role) throws SQLException {
        Statement st = conn.createStatement();
        String temp = "";
        for (String s : name.toLowerCase().split(name)) {
            temp += s.substring(0, 1).toUpperCase() + s.substring(1) + " ";
        }

        st.execute("SELECT * FROM Accounts WHERE id = '" + uname + "';");
        ResultSet srs = st.getResultSet();
        if (srs == null) {
            st.execute("INSERT INTO Accounts (id, name, password, adres, phonenumber, role) "
                    + "VALUES ('" + uname + "', '" + name + "', '" + password + "', '" + address + "', '" + phonenumber + "', '" + role + "');");
        }
        else {
            //To Do
        }
    }
}
