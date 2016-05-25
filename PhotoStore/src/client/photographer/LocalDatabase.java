/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import shared.files.PictureGroup;

public final class LocalDatabase {

    Connection conn;
    File db;

    public LocalDatabase() {
        db = new File("local.dat"); //database location
        try {
            boolean dbExists = db.exists();
            conn = DriverManager.getConnection("jdbc:sqlite:" + db); //set the connection string
            if (!dbExists) {
                //database didnt exist yet
                resetDatabase(); //create the database before usage
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void resetDatabase() {
        ArrayList<String> queries = new ArrayList<>();
        queries.add("DROP TABLE IF EXISTS pictureGroup;");
        queries.add("CREATE TABLE pictureGroup (id INTEGER PRIMARY KEY, obj BLOB, photographer TEXT);");
        queries.add("DROP TABLE IF EXISTS personalid;");
        queries.add("CREATE TABLE personalid (id INTEGER PRIMARY KEY, photographer TEXT);");
        queries.add("DROP TABLE IF EXISTS groupsid");
        queries.add("CREATE TABLE groupid (id INTEGER PRIMARY KEY, photographer TEXT);");
        queries.add("DROP TABLE IF EXISTS photographer;");
        queries.add("CREATE TABLE photographer (id INTEGER PRIMARY KEY, photographerid TEXT, password TEXT);");

        for (String q : queries) {
            try {
                Statement st = conn.createStatement();
                st.executeUpdate(q);
            } catch (SQLException ex) {
                System.out.print(ex.toString());
            }
        }
    }

    public boolean savePhotographer(String photographerid, String password) {
        try {
            String sql = "SELECT * from photographer WHERE id = 1;";
            PreparedStatement ps = conn.prepareStatement(sql);
            if (ps.executeQuery().next()) {
                //picturegroup exists, update is required
                sql = "UPDATE photographer SET photographerid = ?, password = ? WHERE id = 1";
                ps = conn.prepareStatement(sql);
                ps.setString(1, photographerid);
                ps.setString(2, password);
                System.out.println("Updating photographer");
                ps.executeUpdate();
            } else {
                sql = "INSERT INTO photographer (id, photographerid, password) VALUES(1, ?, ?);";
                ps = conn.prepareStatement(sql);
                ps.setString(1, photographerid);
                ps.setString(2, password);
                System.out.println("Insert into photographer");
                ps.executeUpdate();

            }
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;

    }

    public boolean savePictureGroup(PictureGroup pg) {
        try {
            //create a byte array from the picturegroup object
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(pg);
            oos.flush();
            oos.close();
            bos.close();
            byte[] data = bos.toByteArray();
            //check if the picturegroup already exists on the database
            String sql = "SELECT * from pictureGroup WHERE id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pg.getId());
            if (ps.executeQuery().next()) {
                //picturegroup exists, update is required
                sql = "UPDATE pictureGroup SET obj = ?, photographer = ? WHERE id = ?;";
                ps = conn.prepareStatement(sql);
                ps.setObject(1, data);
                ps.setInt(2, pg.getId());
                ps.executeUpdate();
            } else {
                //picturegroup doesnt exist, insert is required
                sql = "INSERT INTO pictureGroup (id, obj, photographer) VALUES(?, ?, ?);";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, pg.getId());
                ps.setObject(2, data);
                ps.setString(3, PhotographerInfo.photographerID);
                ps.executeUpdate();
            }
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean savePersonalID(int id) {
        try {

            /*
                    try {
            String sql = "DELETE from personalid WHERE photographer = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "test@hotmail.com");

            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
             */
            //check if the id already exists on the database
            String sql = "SELECT * from personalid WHERE id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            if (!ps.executeQuery().next()) {
                //personalPicture doesnt exist, insert is required
                sql = "INSERT INTO personalid (id, photographer) VALUES(?, ?);";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, PhotographerInfo.photographerID);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean saveGroupID(int id) {
        try {
            //check if the personalPicture already exists on the database
            String sql = "SELECT * from groupid WHERE id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            if (!ps.executeQuery().next()) {
                //personalPicture doesnt exist, insert is required
                sql = "INSERT INTO groupid (id, photographer) VALUES(?, ?);";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, PhotographerInfo.photographerID);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public List<PictureGroup> getPictureGroups() {
        try {
            //create new list that will contain the picturegroups
            List<PictureGroup> pictureGroups = new ArrayList<>();
            String sql = "SELECT * FROM pictureGroup;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ByteArrayInputStream bais;
                ObjectInputStream ins;
                try {
                    bais = new ByteArrayInputStream(rs.getBytes("obj"));
                    ins = new ObjectInputStream(bais);
                    PictureGroup pg = (PictureGroup) ins.readObject(); //extract picturegroup object from the database
                    pictureGroups.add(pg); //add the picturegroup to the list
                    System.out.println(Integer.toString(pg.getId()));
                    //System.out.println(pg.getPictures().get(0).getCreated() + " - " + pg.getPictures().get(0).getExtension()); //this is for testing
                    ins.close();
                } catch (SQLException | IOException | ClassNotFoundException ex) {
                    Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return pictureGroups;
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Integer> getPersonalID() {
        try {
            //create new list that will contain the picturegroups
            List<Integer> personalid = new ArrayList<>();
            String sql = "SELECT * FROM personalid;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int s = rs.getInt("id");
                personalid.add(s);
            }
            rs.close();
            return personalid;
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Integer> getGroupID() {
        try {
            //create new list that will contain the picturegroups
            List<Integer> groupID = new ArrayList<>();
            String sql = "SELECT * FROM groupid;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int s = rs.getInt("id");
                groupID.add(s);
            }
            return groupID;
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void getPhotographer() {
        String sql = "SELECT * FROM photographer;";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PhotographerInfo.photographerID = rs.getString("photographerid");
                System.out.println(PhotographerInfo.photographerID);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteGroupID(int id) {
        String sql = "DELETE FROM groupid WHERE id = ?";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deletePersonalID(int id) {
        String sql = "DELETE FROM personalid WHERE id = ?";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
