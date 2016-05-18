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
import shared.files.PersonalPicture;
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
        queries.add("CREATE TABLE pictureGroup (id INTEGER PRIMARY KEY, obj BLOB);");
        queries.add("DROP TABLE IF EXISTS personalPicture;");
        queries.add("CREATE TABLE personalPicture (id INTEGER PRIMARY KEY, obj BLOB);");
        for (String q : queries) {
            try {
                Statement st = conn.createStatement();
                st.executeUpdate(q);
            } catch (SQLException ex) {
                System.out.print(ex.toString());
            }
        }
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
                sql = "UPDATE pictureGroup SET obj = ? WHERE id = ?;";
                ps = conn.prepareStatement(sql);
                ps.setObject(1, data);
                ps.setInt(2, pg.getId());
                ps.executeUpdate();
            } else {
                //picturegroup doesnt exist, insert is required
                sql = "INSERT INTO pictureGroup (id, obj) VALUES(?, ?);";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, pg.getId());
                ps.setObject(2, data);
                ps.executeUpdate();
            }
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean savePersonalPicture(PersonalPicture pp) {
        try {
            //create a byte array from the personalPicture object
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(pp);
            oos.flush();
            oos.close();
            bos.close();
            byte[] data = bos.toByteArray();
            //check if the personalPicture already exists on the database
            String sql = "SELECT * from personalPicture WHERE id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pp.getId());
            if (ps.executeQuery().next()) {
                //personalPicture exists, update is required
                sql = "UPDATE personalPicture SET obj = ? WHERE id = ?;";
                ps = conn.prepareStatement(sql);
                ps.setObject(1, data);
                ps.setInt(2, pp.getId());
                ps.executeUpdate();
            } else {
                //personalPicture doesnt exist, insert is required
                sql = "INSERT INTO personalPicture (id, obj) VALUES(?, ?);";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, pp.getId());
                ps.setObject(2, data);
                ps.executeUpdate();
            }
        } catch (IOException | SQLException ex) {
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
    
        public List<PersonalPicture> getPersonalPicture() {
        try {
            //create new list that will contain the picturegroups
            List<PersonalPicture> PersonalPicture = new ArrayList<>();
            String sql = "SELECT * FROM personalPicture;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ByteArrayInputStream bais;
                ObjectInputStream ins;
                try {
                    bais = new ByteArrayInputStream(rs.getBytes("obj"));
                    ins = new ObjectInputStream(bais);
                    PersonalPicture pp = (PersonalPicture) ins.readObject(); //extract picturegroup object from the database
                    PersonalPicture.add(pp); //add the picturegroup to the list
                    System.out.println(Integer.toString(pp.getId()));
                    //System.out.println(pg.getPictures().get(0).getCreated() + " - " + pg.getPictures().get(0).getExtension()); //this is for testing
                    ins.close();
                } catch (SQLException | IOException | ClassNotFoundException ex) {
                    Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return PersonalPicture;
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}