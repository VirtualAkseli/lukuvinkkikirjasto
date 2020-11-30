package kayttoliittyma;

import io.ConsoleIO;
import io.IO;
import tietokanta.Dao;
import vinkkilogic.Book;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utilities.MappingUtils.toMap;

public class Kayttoliittyma {

    Dao<Book, Long> bookDao;
    //private final Scanner lukija;
    private IO io;

    public Kayttoliittyma(Dao<Book, Long> bookDao) {
        this.bookDao = bookDao;
        this.io = new ConsoleIO();
    }

    public void suorita() {
        boolean jatketaan = true;
        listaaKomennot();
        while (jatketaan) {
            String komento = io.readLine("Anna komento!");
            jatketaan = suoritaKomento(komento);
        }
        System.out.println("Lopetetaan...");
    }

    public Boolean suoritaKomento(String komento) {
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
        //System.out.println("Kirjan nimi:");
        String kirjanNimi = io.readLine("Kirjan nimi:");
        //System.out.println("Kirjailija:");
        String kirjailija = io.readLine("Kirjailija:");
        bookDao.create(new Book(kirjanNimi, kirjailija));
        System.out.println("Lisätty!");
        System.out.println("Kirjan nimi: " + kirjanNimi);
        System.out.println("Kirjailija: " + kirjailija);
    }

    private void hae() {
        System.out.println("Hakusana:");
        String hakusana = io.readLine("Hakusana:");
        System.out.println("Hetaan...");
        List<Book> byTitle = bookDao.getByValue(toMap("title", hakusana));
        List<Book> byAuthor = bookDao.getByValue(toMap("author", hakusana));
        System.out.println("Löytyi " + (byTitle.size() + byAuthor.size()) + " hakutulosta.");
        Stream.concat(byTitle.stream(), byAuthor.stream())
                .collect(Collectors.toList()).forEach(System.out::println);
        System.out.println("");
    }

    private void listaa() {
        System.out.println("Kaikki kirjat:");
        bookDao.list().forEach(System.out::println);
    }
}
