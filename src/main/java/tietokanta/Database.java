package tietokanta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private Connection connection;

    public Database() throws SQLException {

        this.connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:h2:./lukuvinkkitietokanta", "sa", "");
            System.out.println("Yhditettiin tietokantaan onnistuneesti");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Books (id INTEGER PRIMARY KEY AUTO_INCREMENT, type TEXT default 'Book', author TEXT, "
                + "title TEXT, isbn TEXT, relatedCourses TEXT, tags TEXT, comments TEXT)");
        stmt.execute();
        stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Videos (id INTEGER PRIMARY KEY AUTO_INCREMENT, type TEXT default 'Video', title TEXT, url TEXT, "
                + "relatedCourses TEXT, tags TEXT, comments TEXT)");
        stmt.execute();
        stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Blogs (id INTEGER PRIMARY KEY AUTO_INCREMENT, type TEXT default 'Blogpost', title TEXT, author TEXT, "
                + "url TEXT, relatedCourses TEXT, tags TEXT, comments TEXT)");
        stmt.execute();
        stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Podcasts (id INTEGER PRIMARY KEY AUTO_INCREMENT, type TEXT default 'Podcast', author TEXT, podcastName TEXT, "
                + "otsikko TEXT, description TEXT, relatedCourses TEXT, tags TEXT, comments TEXT)");
        stmt.execute();

    }

    public void close() throws SQLException {
        try {
            connection.close();
            System.out.println("Yhteys katkaistu onnistuneesti.");
        } catch (Exception e) {
            System.out.println("Virhe tietokannan sulkemisessa: " + e);

        }
    }

    public void addBook(String title, String author) throws SQLException {
        // leikki-valueita tässä vaiheessa kehitystä:
        String isbn = "0";
        String tags = "";
        String relatedCourses = "";

        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Books (author, title, isbn, tags, relatedCourses) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, author);
            stmt.setString(2, title);
            stmt.setString(3, isbn);
            stmt.setString(4, tags);
            stmt.setString(5, relatedCourses);
            stmt.execute();
        } catch (Exception e) {
            System.out.println("Virhe kirjan lisäämisessä: " + e);
        }
    }

    public void listAllBooks() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Books");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            int id = results.getInt("id");
            String type = results.getString("type");
            String title = results.getString("title");
            String author = results.getString("author");
            String isbn = results.getString("isbn");
            String tags = results.getString("tags") != null ? results.getString("tags") : " ";
            String relatedCourses = results.getString("relatedCourses") != null ? results.getString("relatedCourses") : " ";

            System.out.println("Kirjoittaja: " + author + "\nOtsikko: " + title + "\n"
                    + "Tyyppi: " + type + "\nISBN: " + isbn + "\nTagit: " + tags + "\nRelated courses: " + relatedCourses + "\n" + "id: " + id + "\n");
        }
    }

    public void searchForbook(String searchForValue) throws SQLException {
        String searchWildcard = "%" + searchForValue + "%";

        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Books WHERE title LIKE ? OR author LIKE ?");
        stmt.setString(1, searchWildcard);
        stmt.setString(2, searchWildcard);
        ResultSet results = stmt.executeQuery();

        stmt = connection.prepareStatement("SELECT COUNT(*) FROM Books WHERE title LIKE ? OR author LIKE ?");
        stmt.setString(1, searchWildcard);
        stmt.setString(2, searchWildcard);
        ResultSet queryResults = stmt.executeQuery();

        queryResults.last();
        int matchingResults = queryResults.getRow();
        System.out.println("Löytyi " + matchingResults + " hakutulosta.");
        System.out.println("");

        while (results.next()) {
            int id = results.getInt("id");
            String type = results.getString("type");
            String title = results.getString("title");
            String author = results.getString("author");
            String isbn = results.getString("isbn");
            String tags = results.getString("tags") != null ? results.getString("tags") : " ";
            String relatedCourses = results.getString("relatedCourses") != null ? results.getString("relatedCourses") : " ";

            System.out.println("Kirjoittaja: " + author + "\nOtsikko: " + title + "\n"
                    + "Tyyppi: " + type + "\nISBN: " + isbn + "\nTagit: " + tags + "\nRelated courses: " + relatedCourses + "\n" + "id: " + id + "\n");
        }

    }

}
