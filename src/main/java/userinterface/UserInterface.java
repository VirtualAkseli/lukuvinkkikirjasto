package userinterface;

import io.IO;
import database.Dao;
import tiplogic.Tip;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import controller.TipController;

public class UserInterface {

    TipController controller;
    private IO io;

    public UserInterface(TipController controller, IO io) {
        this.controller = controller;
        this.io = io;
    }

    public void run() {
        boolean continuing = true;
        while (continuing) {
            listCommands();
            String command = io.readLine("Valitse toiminto numerolla:");
            continuing = runCommand(command);
        }
        io.print("Lopetetaan...");
    }

    public Boolean runCommand(String command) {
        switch (command) {
            case "1":
                add();
                return true;
            case "2":
                search();
                return true;
            case "3":
                list();
                return true;
            case "4":
                return false;
            default:
                io.print("Virheellinen valinta. Yritä uudestaan.");
                return true;
        }
    }

    private void listCommands() {
        io.print("");
        io.print("Komennot:");
        io.print("1. Lisää uusi lukuvinkki");
        io.print("2. Hae lukuvinkkejä");
        io.print("3. Listaa tallennetut lukuvinkit");
        io.print("4. Lopeta");
        io.print("");
    }

    private void add() {
        String[] tipData = new String[6];
        Arrays.fill(tipData, "");
        while (tipData[0].equals("")) {
            listTypes();
            String command = io.readLine("Valitse vinkin tyyppi numerolla:");
            tipData[0] = giveType(command);
        }
        if (tipData[0].equals("cancel")) {
            return;
        }
        tipData = getInput(tipData);
        //after this tipData has type on 0, author on 1, name on 2, possible identifier on 3, possible url on 4, and comments on 5.
        //and tipData is ready for controller.
        controller.addTip(tipData);
//        tipDao.create(new Tip(type, title, author));
       System.out.println("Lisätty!");
//        System.out.println("Kirjan nimi: " + title);
//        System.out.println("Kirjailija: " + author);
    }

    private void listTypes() {
        io.print("");
        io.print("1. Kirja");
        io.print("2. Podcast");
        io.print("3. Blogi");
        //io.print("4. Video"); 
        io.print("5. Peruuta");
        io.print("");
    }

    public String giveType(String command) {
        switch (command) {
            case "1":
                return "book";
            case "2":
                return "podcast";
            case "3":
                return "blogpost";
//            case "4":
//                return "video";
            case "5":
                return "cancel";
            default:
                io.print("Virheellinen valinta.");
                return "";
        }
    }

    public String[] getInput(String[] tipData) {

        tipData = getMandatoryInfo(tipData);
        while (true) {
            io.print("1. Kyllä");
            io.print("2. En");
            String command = io.readLine("Haluatko antaa lisätietoja?");
            if (command.equals("2")) {
                break;
            } else {
                io.print("Vastaa haluamiisi lisätietokohtiin:");
                tipData = getAdditionalInfo(tipData);
                break;
            }

        }
        io.print("");
        io.print("1. Kyllä");
        io.print("2. Ei");
        String command = io.readLine("Tallennetaanko vinkki näillä tiedoilla?");
        if (command.equals("1")) {
            return tipData;
        } else {
            return getInput(tipData);
        }
    }

    public String[] getMandatoryInfo(String[] tipData) {
        io.print("Vastaathan kaikkiin kohtiin.");
        io.print("");
        switch (tipData[0]) {
            case "book":
                tipData[1] = io.readLine("Kirjailija:");
                tipData[2] = io.readLine("Kirjan nimi:");
                if (tipData[1].equals("") || tipData[2].equals("")) {
                    tipData = getMandatoryInfo(tipData);
                }
                return tipData;
            case "podcast":
                tipData[2] = io.readLine("Podcastin nimi:");
                tipData[4] = io.readLine("Podcastin URL:");
                tipData[5] = io.readLine("Kommentti:");
                if (tipData[2].equals("") || tipData[4].equals("") || tipData[5].equals("")) {
                    tipData = getMandatoryInfo(tipData);
                }
                return tipData;
            case "blogpost":
                tipData[2] = io.readLine("Blogin nimi:");
                tipData[4] = io.readLine("Blogin URL:");
                if (tipData[2].equals("") || tipData[4].equals("")) {
                    tipData = getMandatoryInfo(tipData);
                }
                return tipData;
            default:
                return tipData;
        }
    }

    public String[] getAdditionalInfo(String[] tipData) {
        switch (tipData[0]) {
            case "book":
                tipData[3] = io.readLine("ISBN:");
                tipData[5] = io.readLine("Kommentti:");
                return tipData;
            case "podcast":
                tipData[1] = io.readLine("Podcastin tekijä:");
                tipData[3] = io.readLine("Jakson nimi:");
                return tipData;
            case "blogpost":
                tipData[1] = io.readLine("Blogin pitäjä:");
                tipData[5] = io.readLine("Kommentti:");
                return tipData;
            default:
                return tipData;
        }
    }

    private void search() {
        io.print("Hakusana:");
        String keyword = io.readLine("Hakusana:");
        io.print("Haetaan...");
        List<Tip> results = controller.searchWithWord(keyword);
        io.print("Löytyi " + results.size() + " hakutulosta.");
        results.stream().collect(Collectors.toList()).forEach(item -> io.print(item.toString()));
        io.print("");
    }

    private void list() {
        io.print("Kaikki kirjat:");
        controller.list().forEach(item -> io.print(item.toString()));
    }
}
