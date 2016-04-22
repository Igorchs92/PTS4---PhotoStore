/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.photographer;

/*
 * mysql> CREATE TABLE java_objects ( 
 * id INTEGER PRIMARY KEY, 
 * name varchar(128), 
 * object_value BLOB, 
 * primary key (id));
 **/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import shared.files.Picture;
import shared.files.PictureGroup;

public class LocalDatabaseTest {

   

    public static void main(String args[]) throws Exception {
        LocalDatabase ldbm = new LocalDatabase();
        PictureGroup pg = new PictureGroup(0);
        pg.setName("test");
        pg.addPicture(new Picture("/file.png", "testp", 20));
        ldbm.savePictureGroup(pg);
        ldbm.getPictureGroups();
        //ldbm.resetDatabase();
    }
}
