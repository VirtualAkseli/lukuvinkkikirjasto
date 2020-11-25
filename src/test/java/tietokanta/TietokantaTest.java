/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tietokanta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tata
 */
public class TietokantaTest {

    Connection connection;

    @Before
    public void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2::mem", "sa", "");
        PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Kirjat (id INTEGER PRIMARY KEY AUTO_INCREMENT, tyyppi TEXT default 'Kirja', kirjoittaja TEXT, "
                + "otsikko TEXT, isbn TEXT, liittyvat_kurssit TEXT, tagit TEXT, kommentti TEXT)");
        stmt.execute();

    }

    @Test
    public void tallentaminenHakeminenJaPoistaminenToimii() throws Exception {

        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Kirjat (kirjoittaja, otsikko, isbn) "
                + "VALUES ('Testaaja', 'Testaajan käsikirja', '1111-1111')");
        stmt.execute();

        stmt = connection.prepareStatement("SELECT * FROM Kirjat");
        ResultSet results = stmt.executeQuery();
        results.first();
        assertEquals("Testaaja", results.getString("kirjoittaja"));
        assertEquals("Testaajan käsikirja", results.getString("otsikko"));
        assertEquals("1111-1111", results.getString("isbn"));
        
        stmt = connection.prepareStatement("DELETE FROM Kirjat WHERE kirjoittaja='Testaaja' AND otsikko='Testaajan käsikirja'");
        stmt.execute();
        stmt = connection.prepareStatement("SELECT * FROM Kirjat");
        results = stmt.executeQuery();
        assertEquals(false, results.first()); // resultsetin metodi first palauttaa siis falsen jos ei ole. 
        
    }

}
