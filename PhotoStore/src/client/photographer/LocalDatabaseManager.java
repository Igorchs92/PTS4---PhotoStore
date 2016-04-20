/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalDatabaseManager {

    private Connection conn;
    static final String WRITE_OBJECT_SQL = "INSERT INTO java_objects(name, object_value) VALUES (?, ?)";
    static final String READ_OBJECT_SQL = "SELECT object_value FROM java_objects WHERE id = ?";
    /*
 mysql> CREATE TABLE java_objects ( 
 id INTEGER PRIMARY KEY, 
 name varchar(128), 
 object_value BLOB;
 **/

    private long writeJavaObject(Connection conn, Object object) throws Exception {
    String className = object.getClass().getName();
    PreparedStatement pstmt = conn.prepareStatement(WRITE_OBJECT_SQL);

    // set input parameters
    pstmt.setString(1, className);
    pstmt.setObject(2, object);
    pstmt.executeUpdate();

    // get the generated key for the id
    ResultSet rs = pstmt.getGeneratedKeys();
    int id = -1;
    if (rs.next()) {
      id = rs.getInt(1);
    }

    rs.close();
    pstmt.close();
    System.out.println("writeJavaObject: done serializing: " + className);
    return id;
  }

    private Object readJavaObject(Connection conn, long id) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement(READ_OBJECT_SQL);
    pstmt.setLong(1, id);
    ResultSet rs = pstmt.executeQuery();
    rs.next();
    Object object = rs.getObject(1);
    String className = object.getClass().getName();

    rs.close();
    pstmt.close();
    System.out.println("readJavaObject: done de-serializing: " + className);
    return object;
  }
    
    public void saveObjects(List<Object> objects) throws Exception {
        initConnection();
        for (Object o : objects) {
            if (o instanceof java.io.Serializable) {
            writeJavaObject(conn, o);
            }
            else {
                // maybe throw an error, this object is not serializable
            }
        }
        closeConnection();
    }
    
    public List<Object> getAllObjects() {
        
        return null;
    }
    
    private void initConnection() throws SQLException{
        if (conn != null) closeConnection();
        try{
        conn = DriverManager.getConnection("jdbc:sqlite:PhotoStore_SaveData.db"); //set the connection string
        }
        catch(Exception ex) {
            System.out.println(ex.toString());
        }
        
        
    }
    
    public void resetDatabase(){
        ArrayList<String> queries = new ArrayList<String>();
                
        queries.add("mysql> CREATE TABLE java_objects ( \n" +
        " id INTEGER PRIMARY KEY, \n" +
        " name varchar(128), \n" +
        " object_value BLOB;");
        for(String q: queries)
            {
                try {
                    initConnection();
                    Statement st = conn.createStatement();
                    st.executeUpdate(q);
                } catch (SQLException ex) {
                    System.out.print(ex.toString());
                } finally{
                    closeConnection();
                }
            }
    }

    private void closeConnection() {
        try {
            conn.close();
            conn = null;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
