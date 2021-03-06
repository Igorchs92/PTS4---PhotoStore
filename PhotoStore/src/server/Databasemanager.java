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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import shared.ClientType;
import shared.files.PersonalPicture;
import shared.files.Picture;
import shared.files.PictureGroup;
import shared.user.PhotoItem;
import shared.user.PictureModifies;

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
    public List<Integer> getUniqueNumbers(String photographer) {
        try {
            //Total list of all Uniquenumbers for the photographer
            conn.setAutoCommit(false);
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
            conn.commit();
            conn.setAutoCommit(true);
            return ppl;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex1) {
                Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return null;
        }
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

    public List<Integer> getGroups(String photographer) {
        try {
            //Total list of all groupnumbers for the photographer
            conn.setAutoCommit(false);
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
            conn.commit();
            conn.setAutoCommit(true);
            return gpl;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex1) {
                Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex1);
            }
            return null;
        }
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

    public List<PictureGroup> getUserPictureGroup(String uid) {
        try {
            List<PictureGroup> pgl = new ArrayList<>();
            String sql = "SELECT * FROM personalPictures WHERE user_id = ? GROUP BY group_id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, uid);
            ResultSet srs = ps.executeQuery();
            //create pictureGroups
            while (srs.next()) {
                int pg_id = srs.getInt("group_id");
                if (pg_id != 0) {
                    PictureGroup pg = new PictureGroup(pg_id);

                    //get description
                    String sql2 = "SELECT * FROM groupPictures WHERE id = ?";
                    PreparedStatement ps2 = conn.prepareStatement(sql2);
                    ps2.setInt(1, pg_id);
                    ResultSet srs2 = ps2.executeQuery();
                    if (srs2.next()) {
                        pg.setDescription(srs2.getString("description"));
                    }

                    //add pictures to groupPictures
                    String sql3 = "SELECT * FROM originalPicture op INNER JOIN groupPictures_picture gpp ON gpp.picture_id = op.id WHERE gpp.group_id = ?";
                    PreparedStatement ps3 = conn.prepareStatement(sql3);
                    ps3.setInt(1, pg_id);
                    ResultSet srs3 = ps3.executeQuery();
                    while (srs3.next()) {
                        Picture p = new Picture(srs3.getInt("picture_id"), srs3.getString("extension"), srs3.getString("name"), srs3.getDouble("price"), srs3.getDate("created"));
                        pg.addPicture(p);
                    }

                    //create personalPictures
                    String sql4 = "SELECT * FROM personalPictures WHERE user_id = ? AND group_id = ?";
                    PreparedStatement ps4 = conn.prepareStatement(sql4);
                    ps4.setString(1, uid);
                    ps4.setInt(2, pg_id);
                    ResultSet srs4 = ps4.executeQuery();
                    while (srs4.next()) {
                        int pp_id = srs4.getInt("id");
                        if (pp_id != 0) {
                            PersonalPicture pp = new PersonalPicture(pp_id);

                            //add pictures to personalPictures
                            String sql5 = "SELECT * FROM originalPicture op INNER JOIN personalPictures_picture ppp ON ppp.picture_id = op.id WHERE ppp.personal_id = ?";
                            PreparedStatement ps5 = conn.prepareStatement(sql5);
                            ps5.setInt(1, pp_id);
                            ResultSet srs5 = ps5.executeQuery();
                            while (srs5.next()) {
                                Picture p = new Picture(srs5.getInt("picture_id"), srs5.getString("extension"), srs5.getString("name"), srs5.getDouble("price"), srs5.getDate("created"));
                                pp.addPicture(p);
                            }
                            pg.addPersonalPicture(pp);
                        }
                    }
                    pgl.add(pg);
                }
            }
            return pgl;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public String getPicturePath(String pictureId){
        try {
            String sql = "select pp.group_id, pp.id from personalPictures_picture ppp, " +
                            "originalPicture op, " +
                            "personalPictures pp " +
                            "where ppp.picture_id = op.id " +
                            "and pp.id = ppp.personal_id " +
                            "and op.id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pictureId);
            ResultSet srs = ps.executeQuery();
            while (srs.next()) {
                return "\\" + Long.toString(srs.getLong(1)) + "\\" + Long.toString(srs.getLong(2)) + "\\";
            }
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Integer getNewModifiedPictureId() {
        try {
            String sql = "select COALESCE(MAX(id), 0) from modifiedPicture";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet srs = ps.executeQuery();
            while (srs.next()) {
                return srs.getInt(1) + 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void addModifiedPicture(int id, int pictureId){
        try {
            String sql = "insert into modifiedPicture(id, picture_id) values (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, pictureId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Integer addPictureItemOrder(int modifiedPicture_id, PhotoItem item) {
        try {
            Double picPrice = null;
            String sqlGetPicturePrice = "select op.price from originalPicture op, modifiedPicture mp " +
                                        "where op.id = mp.picture_id " +
                                        "and mp.id = ?";
            PreparedStatement psGetPicturePrice = conn.prepareStatement(sqlGetPicturePrice);
            psGetPicturePrice.setInt(1, modifiedPicture_id);
            ResultSet srsPic = psGetPicturePrice.executeQuery();
            while (srsPic.next()) {
                picPrice = srsPic.getDouble(1);
            }
             
            Double itemPrice = null;
            Integer item_id = null;
            String sqlGetItemPrice = "select id, price from item where name = ?";
            PreparedStatement psGetItemPrice = conn.prepareStatement(sqlGetItemPrice);
            psGetItemPrice.setString(1, item.toString());
            ResultSet srsItem = psGetItemPrice.executeQuery();
            while (srsItem.next()) {
                item_id = srsItem.getInt(1);
                itemPrice = srsItem.getDouble(2);
            }
            
            String sql = "insert into pictureItemOrder(id, item_id, picture_id, item_price, picture_price) " +
                        "values (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setNull(1, java.sql.Types.BIGINT);
            ps.setInt(2, item_id);
            ps.setInt(3, modifiedPicture_id);
            ps.setDouble(4, itemPrice);
            ps.setDouble(5, picPrice);
            ps.execute();
            
            sql = "select last_insert_id()";
            ps = conn.prepareStatement(sql);
            ResultSet srs = ps.executeQuery();
            while (srs.next()) {
                return srs.getInt(1);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Integer addOrderInfo(String userId, int status) {
        try {
            String sql = "insert into orderInfo(id, user_id, ordered, shipped, status)\n" +
                    "values (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setNull(1, java.sql.Types.BIGINT);
            ps.setString(2, userId);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setNull(4, java.sql.Types.DATE);
            ps.setInt(5, status);
            ps.execute();
            
            sql = "select last_insert_id()";
            ps = conn.prepareStatement(sql);
            ResultSet srs = ps.executeQuery();
            while (srs.next()) {
                return srs.getInt(1);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void addOrderInfoPictureItemOrder(int infoId, int pictureItemId) {
        try {
            String sql = "insert into orderInfo_pictureItemOrder(id, info_id, pictureitem_id) " +
                    "values (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setNull(1, java.sql.Types.BIGINT);
            ps.setInt(2, infoId);
            ps.setInt(3, pictureItemId);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap<String, String> getStatistics() {
        try {
            HashMap<String, String> hm = new HashMap<>();
            String sql = "SELECT \n"
                    + "(SELECT COUNT(*) FROM photographer) photographers,\n"
                    + "(SELECT COUNT(*) FROM user) users,\n"
                    + "(SELECT COUNT(id) from groupPictures gp WHERE (SELECT COUNT(*) FROM personalPictures WHERE group_id = gp.id ) > 0) groups,\n"
                    + "(SELECT COUNT(id) from personalPictures pp WHERE (SELECT COUNT(*) FROM personalPictures_picture WHERE personal_id = pp.id ) > 0) uids,\n"
                    + "(SELECT COUNT(*) FROM originalPicture) pictures,\n"
                    + "(SELECT COUNT(*) FROM orderInfo) orders,\n"
                    + "(SELECT IFNULL(ROUND(SUM(item_price) + SUM(picture_price), 2), 0) FROM pictureItemOrder pio, orderInfo_pictureItemOrder oi_pio, orderInfo oi WHERE pio.id = oi_pio.pictureitem_id AND oi.id = oi_pio.info_id) alltime,\n"
                    + "(SELECT IFNULL(ROUND(SUM(item_price) + SUM(picture_price), 2), 0) FROM pictureItemOrder pio, orderInfo_pictureItemOrder oi_pio, orderInfo oi WHERE pio.id = oi_pio.pictureitem_id AND oi.id = oi_pio.info_id AND ordered > DATE_SUB(NOW(), INTERVAL 24 HOUR)) 24h,\n"
                    + "(SELECT IFNULL(ROUND(SUM(item_price) + SUM(picture_price), 2), 0) FROM pictureItemOrder pio, orderInfo_pictureItemOrder oi_pio, orderInfo oi WHERE pio.id = oi_pio.pictureitem_id AND oi.id = oi_pio.info_id AND ordered >= NOW() - INTERVAL 7 DAY) 7d,\n"
                    + "(SELECT IFNULL(ROUND(SUM(item_price) + SUM(picture_price), 2), 0) FROM pictureItemOrder pio, orderInfo_pictureItemOrder oi_pio, orderInfo oi WHERE pio.id = oi_pio.pictureitem_id AND oi.id = oi_pio.info_id AND ordered >= NOW() - INTERVAL 30 DAY) 30d,\n"
                    + "(SELECT IFNULL(ROUND(SUM(item_price) + SUM(picture_price), 2), 0) FROM pictureItemOrder pio, orderInfo_pictureItemOrder oi_pio, orderInfo oi WHERE pio.id = oi_pio.pictureitem_id AND oi.id = oi_pio.info_id AND ordered >= NOW() - INTERVAL 365 DAY) 365d\n"
                    + ";";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet srs = ps.executeQuery();
            srs.next();
            hm.put("photographers", srs.getString("photographers"));
            hm.put("users", srs.getString("users"));
            hm.put("groups", srs.getString("groups"));
            hm.put("uids", srs.getString("uids"));
            hm.put("pictures", srs.getString("pictures"));
            hm.put("orders", srs.getString("orders"));
            hm.put("alltime", srs.getString("alltime"));
            hm.put("24h", srs.getString("24h"));
            hm.put("7d", srs.getString("7d"));
            hm.put("30d", srs.getString("30d"));
            hm.put("365d", srs.getString("365d"));
            return hm;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Pair<String, Double>> Income24h() {
        try {
            List<Pair<String, Double>> pl = new ArrayList<>();
            String sql = "SELECT DATE_FORMAT(hours.hour, '%H') as hour, IFNULL(ROUND(SUM(item_price) + SUM(picture_price)), 0) as earned\n"
                    + "FROM\n"
                    + "  (SELECT NOW() AS hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 1 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 2 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 3 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 4 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 5 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 6 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 7 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 8 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 9 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 10 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 11 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 12 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 13 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 14 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 15 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 16 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 17 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 18 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 19 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 20 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 21 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 22 hour\n"
                    + "   UNION SELECT NOW() - INTERVAL 23 hour) hours\n"
                    + "LEFT JOIN orderInfo oi ON DATE_FORMAT(hours.hour, '%Y-%m-%d %H:') = DATE_FORMAT(oi.ordered, '%Y-%m-%d %H:')\n"
                    + "LEFT JOIN orderInfo_pictureItemOrder oi_pio ON oi.id = oi_pio.info_id\n"
                    + "LEFT JOIN pictureItemOrder pio ON pio.id = oi_pio.pictureitem_id\n"
                    + "GROUP BY hours.hour;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet srs = ps.executeQuery();
            while (srs.next()) {
                pl.add(new Pair(srs.getString("hour"), srs.getDouble("earned")));
            }
            return pl;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Pair<String, Integer>> Pictures7d() {
        try {
            List<Pair<String, Integer>> pl = new ArrayList<>();
            String sql = "SELECT days.day, COUNT(op.id) as pictures\n"
                    + "FROM\n"
                    + "  (SELECT curdate() AS day\n"
                    + "   UNION SELECT CURDATE() - INTERVAL 1 day\n"
                    + "   UNION SELECT CURDATE() - INTERVAL 2 day\n"
                    + "   UNION SELECT CURDATE() - INTERVAL 3 day\n"
                    + "   UNION SELECT CURDATE() - INTERVAL 4 day\n"
                    + "   UNION SELECT CURDATE() - INTERVAL 5 day\n"
                    + "   UNION SELECT CURDATE() - INTERVAL 6 day) days\n"
                    + "LEFT JOIN originalPicture op ON days.day = op.created\n"
                    + "GROUP BY days.day;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet srs = ps.executeQuery();
            while (srs.next()) {
                pl.add(new Pair(srs.getString("day"), srs.getInt("pictures")));
            }
            return pl;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Pair<String, Double>> PhotographersEarned30d() {
        try {
            List<Pair<String, Double>> pl = new ArrayList<>();
            String sql = "SELECT email, IFNULL(ROUND(SUM(earned), 2), 0) earned \n"
                    + "FROM(\n"
                    + "SELECT p.email, SUM(item_price) + SUM(picture_price) earned\n"
                    + "FROM photographer p\n"
                    + "LEFT JOIN groupPictures gp ON gp.photographer_id = p.email\n"
                    + "LEFT JOIN groupPictures_picture gpp ON gp.id = gpp.group_id\n"
                    + "LEFT JOIN originalPicture op ON gpp.picture_id = op.id\n"
                    + "LEFT JOIN modifiedPicture mp ON op.id = mp.picture_id\n"
                    + "LEFT JOIN pictureItemOrder pio ON mp.id = pio.picture_id\n"
                    + "LEFT JOIN orderInfo_pictureItemOrder oi_pio ON pio.id = oi_pio.pictureitem_id\n"
                    + "LEFT JOIN orderInfo oi ON oi_pio.info_id = oi.id\n"
                    + "WHERE ordered >= NOW() - INTERVAL 30 DAY\n"
                    + "GROUP BY p.email\n"
                    + "UNION ALL\n"
                    + "SELECT p.email, SUM(item_price) + SUM(picture_price) earned\n"
                    + "FROM photographer p\n"
                    + "LEFT JOIN personalPictures pp ON pp.photographer_id = p.email\n"
                    + "LEFT JOIN personalPictures_picture ppp ON pp.id = ppp.personal_id\n"
                    + "LEFT JOIN originalPicture op ON ppp.picture_id = op.id\n"
                    + "LEFT JOIN modifiedPicture mp ON op.id = mp.picture_id\n"
                    + "LEFT JOIN pictureItemOrder pio ON mp.id = pio.picture_id\n"
                    + "LEFT JOIN orderInfo_pictureItemOrder oi_pio ON pio.id = oi_pio.pictureitem_id\n"
                    + "LEFT JOIN orderInfo oi ON oi_pio.info_id = oi.id\n"
                    + "WHERE ordered >= NOW() - INTERVAL 30 DAY\n"
                    + "GROUP BY p.email\n"
                    + ") AS x\n"
                    + "GROUP BY email\n"
                    + "ORDER BY earned DESC\n"
                    + "LIMIT 10;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet srs = ps.executeQuery();
            while (srs.next()) {
                pl.add(new Pair(srs.getString("email"), srs.getDouble("earned")));
            }
            return pl;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<Pair<String, Double>> PhotographersSold30d() {
        try {
            List<Pair<String, Double>> pl = new ArrayList<>();
            String sql = "SELECT email, SUM(earned) sold \n"
                    + "FROM(\n"
                    + "SELECT p.email, COUNT(op.id) earned\n"
                    + "FROM photographer p\n"
                    + "LEFT JOIN groupPictures gp ON gp.photographer_id = p.email\n"
                    + "LEFT JOIN groupPictures_picture gpp ON gp.id = gpp.group_id\n"
                    + "LEFT JOIN originalPicture op ON gpp.picture_id = op.id\n"
                    + "LEFT JOIN modifiedPicture mp ON op.id = mp.picture_id\n"
                    + "LEFT JOIN pictureItemOrder pio ON mp.id = pio.picture_id\n"
                    + "LEFT JOIN orderInfo_pictureItemOrder oi_pio ON pio.id = oi_pio.pictureitem_id\n"
                    + "LEFT JOIN orderInfo oi ON oi_pio.info_id = oi.id\n"
                    + "WHERE ordered >= NOW() - INTERVAL 30 DAY\n"
                    + "GROUP BY p.email\n"
                    + "UNION ALL\n"
                    + "SELECT p.email, COUNT(op.id) earned\n"
                    + "FROM photographer p\n"
                    + "LEFT JOIN personalPictures pp ON pp.photographer_id = p.email\n"
                    + "LEFT JOIN personalPictures_picture ppp ON pp.id = ppp.personal_id\n"
                    + "LEFT JOIN originalPicture op ON ppp.picture_id = op.id\n"
                    + "LEFT JOIN modifiedPicture mp ON op.id = mp.picture_id\n"
                    + "LEFT JOIN pictureItemOrder pio ON mp.id = pio.picture_id\n"
                    + "LEFT JOIN orderInfo_pictureItemOrder oi_pio ON pio.id = oi_pio.pictureitem_id\n"
                    + "LEFT JOIN orderInfo oi ON oi_pio.info_id = oi.id\n"
                    + "WHERE ordered >= NOW() - INTERVAL 30 DAY\n"
                    + "GROUP BY p.email\n"
                    + ") AS x\n"
                    + "GROUP BY email\n"
                    + "ORDER BY sold DESC\n"
                    + "LIMIT 10;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet srs = ps.executeQuery();
            while (srs.next()) {
                pl.add(new Pair(srs.getString("email"), srs.getDouble("sold")));
            }
            return pl;
        } catch (SQLException ex) {
            Logger.getLogger(Databasemanager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
