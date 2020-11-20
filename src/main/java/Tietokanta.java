
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tietokanta {
    
    
    public Tietokanta() throws SQLException{
        
        
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:h2:./lukuvinkkitietokanta", "sa", "");
            System.out.println("Yhditettiin tietokantaan onnistuneesti");
        } catch (SQLException ex) {
            Logger.getLogger(Tietokanta.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Kirjat (id INTEGER PRIMARY KEY AUTO_INCREMENT, kirjoittaja TEXT, otsikko TEXT, tyyppi TEXT default 'Kirja', isbn TEXT, tagit TEXT, liittyvat_kurssit TEXT)");
        stmt.execute();
        stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Videot (id INTEGER PRIMARY KEY AUTO_INCREMENT, otsikko TEXT, url TEXT, liittyvat_kurssit TEXT, tyyppi TEXT default 'Video', kommentti TEXT)");
        stmt.execute();
        stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Blogit (id INTEGER PRIMARY KEY AUTO_INCREMENT, otsikko TEXT, kirjoittaja TEXT, url TEXT, tyyppi TEXT default 'Blogpost', liittyvat_kurssit TEXT)");
        stmt.execute();
        stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Podcastit (id INTEGER PRIMARY KEY AUTO_INCREMENT, tekija TEXT, podcastin_nimi TEXT, otsikko TEXT, kuvaus TEXT, tyyppi TEXT default 'Podcast', tagit TEXT, liittyvat_kurssit TEXT)");
        stmt.execute();
        
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
        connection.close();
    }
}