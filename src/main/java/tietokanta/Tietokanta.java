package tietokanta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tietokanta {

    private Connection connection;

    public Tietokanta() throws SQLException {

        this.connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:h2:./lukuvinkkitietokanta", "sa", "");
            System.out.println("Yhditettiin tietokantaan onnistuneesti");
        } catch (SQLException ex) {
            Logger.getLogger(Tietokanta.class.getName()).log(Level.SEVERE, null, ex);
        }

        PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Kirjat (id INTEGER PRIMARY KEY AUTO_INCREMENT, tyyppi TEXT default 'Kirja', kirjoittaja TEXT, "
                + "otsikko TEXT, isbn TEXT, liittyvat_kurssit TEXT, tagit TEXT, kommentti TEXT)");
        stmt.execute();
        stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Videot (id INTEGER PRIMARY KEY AUTO_INCREMENT, tyyppi TEXT default 'Video', otsikko TEXT, url TEXT, "
                + "liittyvat_kurssit TEXT, tagit TEXT, kommentti TEXT)");
        stmt.execute();
        stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Blogit (id INTEGER PRIMARY KEY AUTO_INCREMENT, tyyppi TEXT default 'Blogpost', otsikko TEXT, kirjoittaja TEXT, "
                + "url TEXT, liittyvat_kurssit TEXT, tagit TEXT, kommentti TEXT)");
        stmt.execute();
        stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Podcastit (id INTEGER PRIMARY KEY AUTO_INCREMENT, tyyppi TEXT default 'Podcast', tekija TEXT, podcastin_nimi TEXT, "
                + "otsikko TEXT, kuvaus TEXT, liittyvat_kurssit TEXT, tagit TEXT, kommentti TEXT)");
        stmt.execute();

//        stmt = connection.prepareStatement("INSERT INTO Kirjat (kirjoittaja, otsikko, isbn, tagit, liittyvat_kurssit) VALUES ('testaaja', 'testi', '1111-1111', 'testausta', 'ohjelmistotuotanto')");
//        stmt.execute();
//        stmt = connection.prepareStatement("INSERT INTO Kirjat (kirjoittaja, otsikko, isbn, tagit, liittyvat_kurssit) VALUES ('testaaja', 'testi', '1111-1111', 'testausta', 'ohjelmistotuotanto')");
//        stmt.execute();
//        stmt = connection.prepareStatement("INSERT INTO Kirjat (kirjoittaja, otsikko, isbn, liittyvat_kurssit) VALUES ('testaaja', 'testi', '1111-1111', 'ohjelmistotuotanto')");
//        stmt.execute();
//       
//        stmt = connection.prepareStatement("SELECT * FROM Kirjat");
//        ResultSet results = stmt.executeQuery();
//        while(results.next()){
//            String tyyppi = results.getString("tyyppi");
//            String otsikko = results.getString("otsikko");
//            String kirjoittaja = results.getString("kirjoittaja");
//            String isbn = results.getString("isbn");
//            String tagit = results.getString("tagit") != null ? results.getString("tagit"): " ";
//            String liittyvat_kurssit = results.getString("liittyvat_kurssit") != null ? results.getString("liittyvat_kurssit") : " ";
//            
//            System.out.println("Kirjoittaja: " + kirjoittaja + "\nOtsikko: " + otsikko + "\n"
//                    + "Tyyppi: " + tyyppi + "\nISBN: " + isbn +"\nTagit: "+ tagit +"\nRelated courses: " +  liittyvat_kurssit + "\n");
//       }
    }

    public void sulje() throws SQLException {
        try {
            connection.close();
            System.out.println("Yhteys katkaistu onnistuneesti.");
        } catch (Exception e) {
            System.out.println("Virhe tietokannan sulkemisessa: " + e);

        }
    }

    public void lisaaKirja(String otsikko, String kirjoittaja) throws SQLException {
        // leikki-valueita tässä vaiheessa kehitystä:
        String isbn = "0";
        String tagit = "";
        String liittyvat_kurssit = "";

        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Kirjat (kirjoittaja, otsikko, isbn, tagit, liittyvat_kurssit) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, kirjoittaja);
            stmt.setString(2, otsikko);
            stmt.setString(3, isbn);
            stmt.setString(4, tagit);
            stmt.setString(5, liittyvat_kurssit);
            stmt.execute();
        } catch (Exception e) {
            System.out.println("Virhe kirjan lisäämisessä: " + e);
        }
    }

    public void listaaKaikkiKirjat() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kirjat");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = results.getInt("id");
            String tyyppi = results.getString("tyyppi");
            String otsikko = results.getString("otsikko");
            String kirjoittaja = results.getString("kirjoittaja");
            String isbn = results.getString("isbn");
            String tagit = results.getString("tagit") != null ? results.getString("tagit") : " ";
            String liittyvat_kurssit = results.getString("liittyvat_kurssit") != null ? results.getString("liittyvat_kurssit") : " ";

            System.out.println("Kirjoittaja: " + kirjoittaja + "\nOtsikko: " + otsikko + "\n"
                    + "Tyyppi: " + tyyppi + "\nISBN: " + isbn + "\nTagit: " + tagit + "\nRelated courses: " + liittyvat_kurssit + "\n" + "id: " + id + "\n");
        }
    }

    public void haeKirjaa(String hakusana) throws SQLException {
        String hakusanaVillikorteilla = "%" + hakusana + "%";

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kirjat WHERE otsikko LIKE ? OR kirjoittaja LIKE ?");
        stmt.setString(1, hakusanaVillikorteilla);
        stmt.setString(2, hakusanaVillikorteilla);
        ResultSet results = stmt.executeQuery();

        stmt = connection.prepareStatement("SELECT COUNT(*) FROM Kirjat WHERE otsikko LIKE ? OR kirjoittaja LIKE ?");
        stmt.setString(1, hakusanaVillikorteilla);
        stmt.setString(2, hakusanaVillikorteilla);
        ResultSet maaraResults = stmt.executeQuery();

        maaraResults.last();
        int montakoLoytyi = maaraResults.getRow();
        System.out.println("Löytyi " + montakoLoytyi + " hakutulosta.");
        System.out.println("");

        while (results.next()) {
            int id = results.getInt("id");
            String tyyppi = results.getString("tyyppi");
            String otsikko = results.getString("otsikko");
            String kirjoittaja = results.getString("kirjoittaja");
            String isbn = results.getString("isbn");
            String tagit = results.getString("tagit") != null ? results.getString("tagit") : " ";
            String liittyvat_kurssit = results.getString("liittyvat_kurssit") != null ? results.getString("liittyvat_kurssit") : " ";

            System.out.println("Kirjoittaja: " + kirjoittaja + "\nOtsikko: " + otsikko + "\n"
                    + "Tyyppi: " + tyyppi + "\nISBN: " + isbn + "\nTagit: " + tagit + "\nRelated courses: " + liittyvat_kurssit + "\n" + "id: " + id + "\n");
        }

    }

}
