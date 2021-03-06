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

    private static Connection conn;
    File db;

    public LocalDatabase() {
        db = new File("local.dat"); //database location
        try {
            boolean dbExists = db.exists();
            if (!dbExists) {
                //database didnt exist yet
                resetDatabase(); //create the database before usage
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public Connection getConn() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection("jdbc:sqlite:" + db); //set the connection string
            } catch (SQLException ex) {
                Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return conn;
    }

    public void resetDatabase() {
        ArrayList<String> queries = new ArrayList<>();
        queries.add("DROP TABLE IF EXISTS pictureGroup;");
        queries.add("CREATE TABLE pictureGroup (id INTEGER PRIMARY KEY, obj BLOB, photographer TEXT);");
        queries.add("DROP TABLE IF EXISTS personalid;");
        queries.add("CREATE TABLE personalid (id INTEGER PRIMARY KEY, photographer TEXT);");
        queries.add("DROP TABLE IF EXISTS groupsid;");
        queries.add("CREATE TABLE groupid (id INTEGER PRIMARY KEY, photographer TEXT);");
        queries.add("DROP TABLE IF EXISTS photographer;");
        queries.add("CREATE TABLE photographer (id INTEGER PRIMARY KEY, photographerid TEXT, password TEXT, location TEXT);");
        for (String q : queries) {
            try {
                Statement st = getConn().createStatement();
                st.executeUpdate(q);
            } catch (SQLException ex) {
                System.out.print(ex.toString());
            }
        }
    }

    public boolean savePhotographer(String photographerid, String password) {
        try {
            String sql = "SELECT * from photographer WHERE id = 1;";
            PreparedStatement ps = getConn().prepareStatement(sql);
            if (ps.executeQuery().next()) {
                //picturegroup exists, update is required
                sql = "UPDATE photographer SET photographerid = ?, password = ? WHERE id = 1";
                ps = getConn().prepareStatement(sql);
                ps.setString(1, photographerid);
                ps.setString(2, password);
                System.out.println("Updating photographer");
                ps.executeUpdate();
            } else {
                sql = "INSERT INTO photographer (id, photographerid, password) VALUES(1, ?, ?);";
                ps = getConn().prepareStatement(sql);
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

    public boolean saveLocation(String location) {
        try {
            String sql = "SELECT * from photographer WHERE id = 1;";
            PreparedStatement ps = getConn().prepareStatement(sql);
            if (ps.executeQuery().next()) {
                //picturegroup exists, update is required
                sql = "UPDATE photographer SET location = ?WHERE id = 1";
                ps = getConn().prepareStatement(sql);
                ps.setString(1, location);
                ps.executeUpdate();
            } else {
                sql = "INSERT INTO photographer (id, location) VALUES(1, ?);";
                ps = getConn().prepareStatement(sql);
                ps.setString(1, location);
                ps.executeUpdate();

            }
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;

    }

    public String getLocation() {
        String location = "";
        String sql = "SELECT * FROM photographer;";
        PreparedStatement ps;
        try {
            ps = getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                location = rs.getString("location");
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return location;
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
            PreparedStatement ps = getConn().prepareStatement(sql);
            ps.setInt(1, pg.getId());
            if (ps.executeQuery().next()) {
                //picturegroup exists, update is required
                sql = "UPDATE pictureGroup SET obj = ?, photographer = ? WHERE id = ?;";
                ps = getConn().prepareStatement(sql);
                ps.setObject(1, data);
                ps.setString(2, PhotographerInfo.photographerID);
                ps.setInt(3, pg.getId());
                ps.executeUpdate();
            } else {
                //picturegroup doesnt exist, insert is required
                sql = "INSERT INTO pictureGroup (id, obj, photographer) VALUES(?, ?, ?);";
                ps = getConn().prepareStatement(sql);
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

    public boolean savePictureGroupList(List<PictureGroup> pgl) {
        try {
            getConn().setAutoCommit(false);
            for (PictureGroup pg : pgl) {
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
                PreparedStatement ps = getConn().prepareStatement(sql);
                ps.setInt(1, pg.getId());
                if (ps.executeQuery().next()) {
                    //picturegroup exists, update is required
                    sql = "UPDATE pictureGroup SET obj = ?, photographer = ? WHERE id = ?;";
                    ps = getConn().prepareStatement(sql);
                    ps.setObject(1, data);
                    ps.setString(2, PhotographerInfo.photographerID);
                    ps.setInt(3, pg.getId());
                    ps.executeUpdate();
                } else {
                    //picturegroup doesnt exist, insert is required
                    sql = "INSERT INTO pictureGroup (id, obj, photographer) VALUES(?, ?, ?);";
                    ps = getConn().prepareStatement(sql);
                    ps.setInt(1, pg.getId());
                    ps.setObject(2, data);
                    ps.setString(3, PhotographerInfo.photographerID);
                    ps.executeUpdate();
                }
            }
            getConn().commit();
            getConn().setAutoCommit(true);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean savePersonalPictureId(int id) {
        try {
            //check if the id already exists on the database
            String sql = "SELECT * from personalid WHERE id = ?;";
            PreparedStatement ps = getConn().prepareStatement(sql);
            ps.setInt(1, id);
            if (!ps.executeQuery().next()) {
                //personalPicture doesnt exist, insert is required
                sql = "INSERT INTO personalid (id, photographer) VALUES(?, ?);";
                ps = getConn().prepareStatement(sql);
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

    public boolean savePersonalPictureIdList(List<Integer> idl) {
        try {
            getConn().setAutoCommit(false);
            //check if the personalPicture already exists on the database
            String bsql = "INSERT INTO personalid (id, photographer) VALUES(?, ?);";
            PreparedStatement bps = getConn().prepareStatement(bsql);
            for (int id : idl) {
                String sql = "SELECT * from personalid WHERE id = ?;";
                PreparedStatement ps = getConn().prepareStatement(sql);
                ps.setInt(1, id);
                if (!ps.executeQuery().next()) {
                    bps.setInt(1, id);
                    bps.setString(2, PhotographerInfo.photographerID);
                    bps.addBatch();
                }
            }
            bps.executeBatch();
            getConn().commit();
            getConn().setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean savePictureGroupId(int id) {
        try {
            //check if the personalPicture already exists on the database
            String sql = "SELECT * from groupid WHERE id = ?;";
            PreparedStatement ps = getConn().prepareStatement(sql);
            ps.setInt(1, id);
            if (!ps.executeQuery().next()) {
                sql = "SELECT * from pictureGroup WHERE id = ?;";
                ps = getConn().prepareStatement(sql);
                ps.setInt(1, id);
                if (!ps.executeQuery().next()) {
                    //personalPicture doesnt exist, insert is required
                    sql = "INSERT INTO groupid (id, photographer) VALUES(?, ?);";
                    ps = getConn().prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.setString(2, PhotographerInfo.photographerID);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean savePictureGroupIdList(List<Integer> idl) {
        try {
            getConn().setAutoCommit(false);
            //check if the personalPicture already exists on the database
            String bsql = "INSERT INTO groupid (id, photographer) VALUES(?, ?);";
            PreparedStatement bps = getConn().prepareStatement(bsql);
            for (int id : idl) {
                String sql = "SELECT * from groupid WHERE id = ?;";
                PreparedStatement ps = getConn().prepareStatement(sql);
                ps.setInt(1, id);
                if (!ps.executeQuery().next()) {
                    sql = "SELECT * from pictureGroup WHERE id = ?;";
                    ps = getConn().prepareStatement(sql);
                    ps.setInt(1, id);
                    if (!ps.executeQuery().next()) {
                        //personalPicture doesnt exist, insert is required
                        bps.setInt(1, id);
                        bps.setString(2, PhotographerInfo.photographerID);
                        bps.addBatch();
                    }
                }
            }
            bps.executeBatch();
            getConn().commit();
            getConn().setAutoCommit(true);
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
            String sql = "SELECT * FROM pictureGroup WHERE photographer = ?;";
            PreparedStatement ps = getConn().prepareStatement(sql);
            ps.setString(1, PhotographerInfo.photographerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ByteArrayInputStream bais;
                ObjectInputStream ins;
                try {
                    bais = new ByteArrayInputStream(rs.getBytes("obj"));
                    ins = new ObjectInputStream(bais);
                    PictureGroup pg = (PictureGroup) ins.readObject(); //extract picturegroup object from the database
                    pictureGroups.add(pg); //add the picturegroup to the list
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
            String sql = "SELECT * FROM personalid WHERE photographer = ?;";
            PreparedStatement ps = getConn().prepareStatement(sql);
            ps.setString(1, PhotographerInfo.photographerID);
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
            String sql = "SELECT * FROM groupid WHERE photographer = ?;";
            PreparedStatement ps = getConn().prepareStatement(sql);
            ps.setString(1, PhotographerInfo.photographerID);
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
            ps = getConn().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PhotographerInfo.photographerID = rs.getString("photographerid");
                PhotographerInfo.photographerPass = rs.getString("password");
                System.out.println(PhotographerInfo.photographerID);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removePictureGroup(int id) {
        String sql = "DELETE FROM pictureGroup WHERE id = ?";
        PreparedStatement ps;
        try {
            ps = getConn().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removePictureGroupId(int id) {
        String sql = "DELETE FROM groupid WHERE id = ?";
        PreparedStatement ps;
        try {
            ps = getConn().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removePersonalPictureId(int id) {
        String sql = "DELETE FROM personalid WHERE id = ?";
        PreparedStatement ps;
        try {
            ps = getConn().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removePersonalPictureIdList(List<Integer> idl) {
        try {
            getConn().setAutoCommit(false);
            String sql = "DELETE FROM personalid WHERE id = ?";
            PreparedStatement ps = getConn().prepareStatement(sql);
            for (int id : idl) {
                ps.setInt(1, id);
                ps.addBatch();
            }
            ps.executeBatch();
            getConn().commit();
            getConn().setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(LocalDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
