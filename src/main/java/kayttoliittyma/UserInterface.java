package kayttoliittyma;

import io.ConsoleIO;
import io.IO;
import tietokanta.Dao;
import vinkkilogic.Book;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utilities.MappingUtils.toMap;

public class UserInterface {

    Dao<Book, Long> bookDao;
    private IO io;

    public UserInterface(Dao<Book, Long> bookDao) {
        this.bookDao = bookDao;
        this.io = new ConsoleIO();
    }

    public void run() {
        boolean continuing = true;
        listCommands();
        while (continuing) {
            String command = io.readLine("Anna komento!");
            continuing = runCommand(command);
        }
        System.out.println("Lopetetaan...");
    }

    public Boolean runCommand(String command) {
        switch (command) {
            case "lisaa":
                add();
                return true;
            case "hae":
                search();
                return true;
            case "listaa":
                list();
                return true;
            case "x":
                return false;
            default:
                System.out.println("Virheellinen komento. Yritä uudestaan.");
                return true;
        }
    }

    private void listCommands() {
        System.out.println("");
        System.out.println("Komennot:");
        System.out.println("lisaa = lisää uusi lukuvinkki");
        System.out.println("hae = hae listalta");
        System.out.println("listaa = listaa kaikki lukuvinkit");
        System.out.println("x = lopeta");
        System.out.println("");
    }

    private void add() {
        String title = io.readLine("Kirjan nimi:");
        String author = io.readLine("Kirjailija:");
        bookDao.create(new Book(title, author));
        System.out.println("Lisätty!");
        System.out.println("Kirjan nimi: " + title);
        System.out.println("Kirjailija: " + author);
    }

    private void search() {
        System.out.println("Hakusana:");
        String keyword = io.readLine("Hakusana:");
        System.out.println("Haetaan...");
        List<Book> byTitle = bookDao.getByValue(toMap("title", keyword));
        List<Book> byAuthor = bookDao.getByValue(toMap("author", keyword));
        System.out.println("Löytyi " + (byTitle.size() + byAuthor.size()) + " hakutulosta.");
        Stream.concat(byTitle.stream(), byAuthor.stream())
                .collect(Collectors.toList()).forEach(System.out::println);
        System.out.println("");
    }

    private void list() {
        System.out.println("Kaikki kirjat:");
        bookDao.list().forEach(System.out::println);
    }
}
