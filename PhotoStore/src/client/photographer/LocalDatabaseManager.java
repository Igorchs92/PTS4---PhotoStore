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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalDatabaseManager {

    private Properties props;
    private Connection conn;


    
    public void save(Object o) throws IOException {
        try {
            //todo opgave 4
            resetDatabase();
            initConnection();            
            String insertIntoPersoon = "insert into persoon (persoonsnummer, achternaam, voornamen, tussenvoegsel, geboortedatum, geboorteplaats, geslacht) " +
                    "values ";
            String insertIntoGezin = "insert into gezin (gezinsnummer, ouder1, ouder2, huwelijksdatum, scheidingsdatum) values ";
            
            for (Persoon p : admin.getPersonen())
            {
                String query = insertIntoPersoon + "(?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement st = conn.prepareStatement(query);
                st.setInt(1, p.getNr());
                st.setString(2, p.getAchternaam());
                st.setString(3, p.getVoornamen());
                st.setString(4, p.getTussenvoegsel());
                st.setString(5, (new Timestamp(p.getGebDat().getTimeInMillis())).toString());
                st.setString(6, p.getGebPlaats());
                st.setString(7, p.getGeslacht().toString());
                st.execute();
                
                if (p.getOuderlijkGezin() != null) {
                    query = "update persoon set ouders= ? where persoonsnummer = ?;";
                    PreparedStatement st2 = conn.prepareStatement(query);
                    st2.setInt(1, p.getOuderlijkGezin().getNr());
                    st2.setInt(2, p.getNr());
                    st2.execute();
                }
            }
            
            for(Gezin gezin : admin.getGezinnen()) {
                String query = insertIntoGezin + "(?, ?, ?, ?, ?);";
                
                PreparedStatement st = conn.prepareStatement(query);
                st.setInt(1, gezin.getNr());
                st.setInt(2, gezin.getOuder1().getNr());
                if (gezin.getOuder2() == null) st.setNull(3, java.sql.Types.INTEGER);
                else st.setInt(3, gezin.getOuder2().getNr());
                if (gezin.getHuwelijksdatum() == null) st.setNull(4, java.sql.Types.TIMESTAMP);
                else st.setString(4, (new Timestamp(gezin.getHuwelijksdatum().getTimeInMillis())).toString());
                if (gezin.getScheidingsdatum() == null) st.setNull(5, java.sql.Types.TIMESTAMP);
                else st.setString(5, (new Timestamp(gezin.getScheidingsdatum().getTimeInMillis())).toString());
                st.execute();
            }
            
        } catch (Exception ex) {
            Logger.getLogger(DatabaseMediator.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            closeConnection();
        }
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
    
    private void resetDatabase(){
        ArrayList<String> queries = new ArrayList<String>();
        queries.add("drop table if exists Persoon;");
        queries.add("drop table if exists Gezin;");
        queries.add("CREATE TABLE Persoon (persoonsNummer INT PRIMARY KEY NOT NULL, achternaam STRING NOT NULL, voornamen STRING NOT NULL, tussenvoegsel STRING, geboortedatum DATETIME NOT NULL, geboorteplaats STRING NOT NULL, geslacht STRING NOT NULL);");
        queries.add("CREATE TABLE Gezin (gezinsNummer INTEGER PRIMARY KEY NOT NULL, ouder1 INTEGER REFERENCES Persoon (persoonsNummer) NOT NULL, ouder2 INTEGER REFERENCES Persoon (persoonsNummer), huwelijksdatum DATETIME, scheidingsdatum DATETIME);");
        queries.add("alter table Persoon add column ouders integer references Gezin(GezinsNummer);");
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
