package controller;

import tietokanta.Database;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import kayttoliittyma.Kayttoliittyma;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello Lukuvinkkikirjasto!");

        System.out.println("Yhdistetään tietokantaan");
        try {
            Database tietokanta = new Database();
            Kayttoliittyma kl = new Kayttoliittyma(tietokanta);

            kl.suorita();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
