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

public final class LocalDatabaseManager {

    Connection conn;
    File db;

    public LocalDatabaseManager() {
        db = new File("save.dat");
        try {
            boolean dbExists = db.exists();
            conn = DriverManager.getConnection("jdbc:sqlite:" + db); //set the connection string
            if (!dbExists) {
                resetDatabase();
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void resetDatabase() {
        ArrayList<String> queries = new ArrayList<>();
        queries.add("drop table if exists pictureGroup");
        queries.add("CREATE TABLE pictureGroup (id INTEGER PRIMARY KEY, obj BLOB)");
        for (String q : queries) {
            try {
                Statement st = conn.createStatement();
                st.executeUpdate(q);
            } catch (SQLException ex) {
                System.out.print(ex.toString());
            }
        }
    }

    public boolean savePictureGroup(PictureGroup pg) throws Exception {
        try {
            PreparedStatement ps;
            String sql;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(pg);
            oos.flush();
            oos.close();
            bos.close();
            byte[] data = bos.toByteArray();
            sql = "SELECT * from pictureGroup WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, pg.getId());
            if (ps.executeQuery().next()) {
                sql = "UPDATE pictureGroup SET obj = ? WHERE id = ?";
                ps = conn.prepareStatement(sql);
                ps.setObject(1, data);
                ps.setInt(2, pg.getId());
                ps.executeUpdate();
            } else {
                sql = "INSERT INTO pictureGroup (id, obj) VALUES(?, ?);";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, pg.getId());
                ps.setObject(2, data);
                ps.executeUpdate();
            }
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LocalDatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public List<PictureGroup> getPictureGroups() {
        try {
            Object rmObj = null;
            PreparedStatement ps;
            ResultSet rs;
            String sql;
            List<PictureGroup> pictureGroups = new ArrayList<>();
            sql = "select * from pictureGroup;";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ByteArrayInputStream bais;
                ObjectInputStream ins;
                try {
                    bais = new ByteArrayInputStream(rs.getBytes("obj"));
                    ins = new ObjectInputStream(bais);
                    PictureGroup pg = (PictureGroup) ins.readObject();
                    pictureGroups.add(pg);
                    System.out.println(pg.getGroupPictures().get(0).getCreated() + " - " + pg.getGroupPictures().get(0).getExtension());
                    ins.close();
                } catch (SQLException | IOException | ClassNotFoundException ex) {
                    Logger.getLogger(LocalDatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return pictureGroups;
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
