package kayttoliittyma;

import java.sql.SQLException;
import tietokanta.Tietokanta;
import java.util.Scanner;

public class Kayttoliittyma {

    private Scanner lukija;
    private Tietokanta tietokanta;

    public Kayttoliittyma(Tietokanta tietokanta) {
        this.lukija = new Scanner(System.in);
        this.tietokanta = tietokanta;
    }

    public void suorita() throws SQLException {
        boolean jatketaan = true;
        listaaKomennot();
        while (jatketaan) {
            System.out.println("Anna komento!");

            String komento = lukija.nextLine();
            jatketaan = suoritaKomento(komento);
        }
        System.out.println("Lopetetaan...");
        tietokanta.sulje();
    }

    public Boolean suoritaKomento(String komento) throws SQLException {
        switch (komento) {
            case "lisaa":
                lisaa();
                return true;
            case "hae":
                hae();
                return true;
            case "listaa":
                listaa();
                return true;
            case "x":
                return false;
            default:
                System.out.println("Virheellinen komento. Yritä uudestaan.");
                return true;
        }
    }
    
    private void listaaKomennot() {
        System.out.println("");
        System.out.println("Komennot:");
        System.out.println("lisaa = lisää uusi lukuvinkki");
        System.out.println("hae = hae listalta");
        System.out.println("listaa = listaa kaikki lukuvinkit");
        System.out.println("x = lopeta");    
        System.out.println("");
    }
        
    private void lisaa() {
        System.out.println("Kirjan nimi:");
        String kirjanNimi = lukija.nextLine();
        System.out.println("Kirjailija:");
        String kirjailija = lukija.nextLine();
        // tähän koodi joka todella lisää kirjan tietokantaan
        tietokanta.lisaaKirja(kirjanNimi, kirjailija);
        
        
        System.out.println("Lisätty!");
        System.out.println("Kirjan nimi: " + kirjanNimi);
        System.out.println("Kirjailija: " + kirjailija);
    }

    private void hae() {
        System.out.println("Hakusana:");
        String hakusana = lukija.nextLine();
        System.out.println("Hetaan...");
        // tähän koodi joka todella hakee tietokannasta
        System.out.println("Haku sanalla " + hakusana + " tuotti 0 tulosta.");
    }

    private void listaa() throws SQLException {
        System.out.println("Kaikki kirjat:");
        tietokanta.listaaKaikkiKirjat();
    }
   

}
