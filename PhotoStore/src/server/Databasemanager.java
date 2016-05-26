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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ClientType;
import shared.files.PersonalPicture;
import shared.files.Picture;
import shared.files.PictureGroup;

/**
 *
 * @author Jeroen0606 & Igor
 */
public class Databasemanager {

    Connection conn;

    public Databasemanager() {
        String url = "jdbc:mysql://db4free.net:3306/";
        String dbName = "pts4photostore";
        String userName = "pts4";
        String password = "photostore";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url + dbName, userName, password);
        } catch (Exception ex) {
            System.out.println("Connecting to database failed");
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    public boolean login(ClientType type, String email, String password) {
        try {
            String sql = "SELECT * FROM `" + type.toString() + "` WHERE email = ? AND password = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email.toLowerCase());
            ps.setString(2, password);
            return ps.executeQuery().next();
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean registerUser(String email, String password, String name, String address, String zipcode, String city, String country, String phone) {
        try {
            String sql = "INSERT INTO user(email, password, name, address, zipcode, city, country, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email.toLowerCase());
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, address);
            ps.setString(5, zipcode);
            ps.setString(6, city);
            ps.setString(7, country);
            ps.setString(8, phone);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean registerPhotographer(String email, String password, String name, String address, String zipcode, String city, String country, String phone, String kvk) {
        try {
            System.out.println("Hij komt hier 1");
            String sql = "INSERT INTO photographer(email, password, name, address, zipcode, city, country, phone, kvk)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email.toLowerCase());
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, address);
            ps.setString(5, zipcode);
            ps.setString(6, city);
            ps.setString(7, country);
            ps.setString(8, phone);
            ps.setString(9, kvk);
            System.out.println("Hij komt hier 2");
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean registerProducer(String email, String password, String name) {
        try {
            String sql = "INSERT INTO producer(email, password, name) VALUES (?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email.toLowerCase());
            ps.setString(2, password);
            ps.setString(3, name);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean createPhotographer(String email, String auth) {
        try {
            String sql = "INSERT INTO photographer(email, password) VALUES (?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email.toLowerCase());
            ps.setString(2, auth);
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class
                    .getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public int addOriginalPicture(Picture picture) {
        try {
            String sql = "INSERT INTO originalPicture(extension, name, price, created) VALUES (?, ?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, picture.getExtension());
            ps.setString(2, picture.getName());
            ps.setDouble(3, picture.getPrice());
            ps.setDate(4, convertJavaDateToSqlDate(picture.getCreated()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class
                    .getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public boolean addGroupPicturesPicture(PictureGroup pg, Picture p) {
        try {
            String sql = "INSERT INTO groupPictures_picture (group_id, picture_id) VALUES (?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pg.getId());
            ps.setInt(2, p.getId());
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class
                    .getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean addPersonalPicturesPicture(PersonalPicture pp, Picture p) {
        try {
            String sql = "INSERT INTO personalPictures_picture (personal_id, picture_id) VALUES (?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pp.getId());
            ps.setInt(2, p.getId());
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class
                    .getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean modifyPersonalPicture(PictureGroup pg, PersonalPicture pp) {
        try {
            String sql = "UPDATE personalPictures SET group_id = ? WHERE id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pg.getId());
            ps.setInt(2, pp.getId());
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class
                    .getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean modifyGroupPictureInfo(PictureGroup group) {
        try {
            String sql = "UPDATE groupPictures SET name = ?, description = ? WHERE id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, group.getName());
            ps.setString(2, group.getDescription());
            ps.setInt(3, group.getId());
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean modifyPicture(Picture picture) {
        try {
            String sql = "UPDATE originalPicture SET name = ?, price = ? WHERE id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, picture.getName());
            ps.setDouble(2, picture.getPrice());
            ps.setInt(3, picture.getId());
            return ps.executeUpdate() != 0;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    //Make new unique numbers with the given photographer and return the list.
    public List<Integer> getUniqueNumbers(String photographer) throws SQLException {
        //Total list of all Uniquenumbers for the photographer
        ArrayList<Integer> ppl = new ArrayList<>();
        int count = 0;
        String sql = "SELECT * FROM personalPictures WHERE photographer_id = ? AND group_id is null";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, photographer);
        //select the not used unique numbers where th photographer is equal to the given photographer.
        ResultSet srs = ps.executeQuery();
        //count how many unique numbers are not used.
        while (srs.next()) {
            count += 1;
            ppl.add(srs.getInt("id"));
        }
        System.out.println("personalPictures not in use: " + count);
        //make new unique numbers
        sql = "INSERT INTO personalPictures(photographer_id) VALUES (?);";
        ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        //Make new unique numbers till we have the 10.000 numbers
        while (count < 1000) {
            ps.setString(1, photographer);
            ps.addBatch();
            count++;
        }
        ps.executeBatch();
        ResultSet rs = ps.getGeneratedKeys();
        while (rs.next()) {
            ppl.add(rs.getInt(1));
        }
        return ppl;
    }

    //edit the groups
    public void editGroup(String photographer_id, String name, String description) throws SQLException {

        try {
            Statement st = conn.createStatement();

            //select the not used unique numbers where the photographer is equal to the given photographer.
            String query = ("INSERT INTO groupPictures (photographer_id, name, description)" + " VALUES (?,?,?)");
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, photographer_id);
            ps.setString(2, name);
            ps.setString(3, description);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //make 500 groups for the given photographer
    public void makeTonsOfGroup(String photographer_id) throws SQLException {

        int count = 0;
        //
        Statement st = conn.createStatement();
        //
        ResultSet srs = st.executeQuery("SELECT * FROM groupPictures WHERE photographer_id = '" + photographer_id + "'");
        System.out.println(srs.getFetchSize());

        //count how many unique numbers are used.
        while (srs.next()) {
            count += 1;
        }

        PreparedStatement ps;
        st = conn.createStatement();

        //select the not used unique numbers where the photographer is equal to the given photographer.
        String query = ("INSERT INTO groupPictures (photographer_id)" + " VALUES (?)");
        ps = conn.prepareStatement(query);
        while (count < 500) {
            try {
                ps.setString(1, photographer_id);
                ps.execute();

                count++;
            } catch (SQLException ex) {
                Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ps.close();
    }

    public List<Integer> getGroups(String photographer) throws SQLException {
        //Total list of all groupnumbers for the photographer
        ArrayList<Integer> gpl = new ArrayList<>();
        int count = 0;
        String sql = "SELECT gp.id, COUNT(pp.id) + COUNT(gpp.id) AS amount FROM groupPictures gp "
                + "LEFT JOIN personalPictures pp ON gp.id = pp.group_id "
                + "LEFT JOIN groupPictures_picture gpp ON gp.id = gpp.group_id "
                + "WHERE gp.photographer_id = ? "
                + "GROUP BY gp.id HAVING amount <= 0;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, photographer);
        //select the not used group numbers where th photographer is equal to the given photographer.
        ResultSet srs = ps.executeQuery();
        //count how many group numbers are not used.
        while (srs.next()) {
            count += 1;
            gpl.add(srs.getInt("id"));
        }
        System.out.println("groupPictures not in use: " + count);
        //make new group numbers
        sql = "INSERT INTO groupPictures(photographer_id, name, description) VALUES (?, '', '');";
        ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        //Make new group numbers till we have the 500 numbers
        while (count < 500) {
            ps.setString(1, photographer);
            ps.addBatch();
            count++;
        }
        ps.executeBatch();
        ResultSet rs = ps.getGeneratedKeys();
        while (rs.next()) {
            gpl.add(rs.getInt(1));
        }
        return gpl;
    }

    //add groups to the personal unique number
    public void addGroupToUniqueNumber(int group_id, int personalPictures_id) throws SQLException {
        Statement st = conn.createStatement();

        // create the mysql update preparedstatement
        String query = "UPDATE personalPictures set group_id = ? where id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, group_id);
        ps.setInt(2, personalPictures_id);
        System.out.println(group_id + " pers number   " + personalPictures_id);
        // execute the java preparedstatement
        ps.executeUpdate();
        st.close();
    }

    public void attachCodeToAccount(String user_id, int personalPictures_id) throws SQLException {

        Statement st = conn.createStatement();

        // create the mysql update preparedstatement
        String query = "UPDATE personalPictures set user_id = ? where id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, user_id);
        ps.setInt(2, personalPictures_id);
        // execute the java preparedstatement
        ps.executeUpdate();
        st.close();
    }

    //Change price of the given picture
    public void changePicturePrice(int id, double price) throws SQLException {
        Statement st = conn.createStatement();
        String query = "UPDATE originalPicture set price = ? where id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setDouble(1, price);
        ps.setInt(2, id);
        ps.executeUpdate();
        st.close();
    }
}
