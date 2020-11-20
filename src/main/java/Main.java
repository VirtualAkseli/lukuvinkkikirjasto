
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello Lukuvinkkikirjasto!");

        System.out.println("Yhdistetään tietokantaan");
        try {
            Tietokanta tietokanta = new Tietokanta();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
