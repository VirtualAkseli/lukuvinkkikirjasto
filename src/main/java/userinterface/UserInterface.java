package kayttoliittyma;

import io.ConsoleIO;
import io.IO;
import java.util.Arrays;
import tietokanta.Dao;
import vinkkilogic.Tip;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utilities.MappingUtils.toMap;

public class UserInterface {

    Dao<Tip, Long> tipDao;
    private IO io;

    public UserInterface(Dao<Tip, Long> tipDao, IO io) {
        this.tipDao = tipDao;
        this.io = io;
    }

    public void run() {
        boolean continuing = true;
        while (continuing) {
            listCommands();
            String command = io.readLine("Valitse toiminto numerolla:");
            continuing = runCommand(command);
        }
        System.out.println("Lopetetaan...");
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
                System.out.println("Virheellinen valinta. Yritä uudestaan.");
                return true;
        }
    }

    private void listCommands() {
        System.out.println("");
        System.out.println("Komennot:");
        System.out.println("1. Lisää uusi lukuvinkki");
        System.out.println("2. Hae lukuvinkkejä");
        System.out.println("3. Listaa tallennetut lukuvinkit");
        System.out.println("4. Lopeta");
        System.out.println("");
    }
    
    private void add() {
        String[] tipData = new String[6];
        Arrays.fill(tipData, "");
        while(tipData[0].equals("")){
            listTypes();
            String command = io.readLine("Valitse vinkin tyyppi numerolla:");
            tipData[0] = giveType(command);
        }
        tipData = getInput(tipData);
        //after this tipData has type on 0, author on 1, name on 2, possible identifier on 3, possible url on 4, and comments on 5.
        //and tipData is ready for controller.
        
        
//        tipDao.create(new Tip(type, title, author));
//        System.out.println("Lisätty!");
//        System.out.println("Kirjan nimi: " + title);
//        System.out.println("Kirjailija: " + author);
    }

        private void listTypes(){
        System.out.println("");
        System.out.println("1. Kirja");
        System.out.println("2. Podcast");
        System.out.println("3. Blogi");
       //System.out.println("4. Video"); 
        System.out.println("5. Peruuta");
        System.out.println("");
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
            default:
                System.out.println("Virheellinen valinta.");
                return "";
        }
    }
    
    public String[] getInput(String[] tipData){

        tipData = getMandatoryInfo(tipData);
        while(true){
            System.out.println("1. Kyllä");
            System.out.println("2. En");
            String command = io.readLine("Haluatko antaa lisätietoja?");
            if(command.equals("2")) break;
            else {
                System.out.println("Vastaa haluamiisi lisätietokohtiin:");
                tipData = getAdditionalInfo(tipData);
                break;
            }
            
        }
        System.out.println("");
        System.out.println("1. Kyllä");
        System.out.println("2. Ei");
        String command = io.readLine("Tallennetaanko vinkki näillä tiedoilla?");
        if(command.equals("1")) return tipData;
        else {
        return getInput(tipData);
        }
    }
    
    public String[] getMandatoryInfo(String[] tipData){
        System.out.println("Vastaathan kaikkiin kohtiin.");
        System.out.println("");
        switch(tipData[0]){
            case "book":
                tipData[1] = io.readLine("Kirjailija:");
                tipData[2] = io.readLine("Kirjan nimi:");
                if(tipData[1].equals("") || tipData[2].equals("")) tipData = getMandatoryInfo(tipData);
                return tipData;
            case "podcast":
                tipData[2] = io.readLine("Podcastin nimi:");
                tipData[4] = io.readLine("Podcastin URL:");
                tipData[5] = io.readLine("Kommentti:");
                if(tipData[2].equals("") || tipData[4].equals("") || tipData[5].equals("")) tipData = getMandatoryInfo(tipData);
                return tipData;
            case "blogpost":
                tipData[2] = io.readLine("Blogin nimi:");
                tipData[4] = io.readLine("Blogin URL:");
                if(tipData[2].equals("") || tipData[4].equals("")) tipData = getMandatoryInfo(tipData);
                return tipData;
            default:
                return tipData;
        }          
    }
    
    public String[] getAdditionalInfo(String[] tipData){
        switch(tipData[0]){
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
        System.out.println("Hakusana:");
        String keyword = io.readLine("Hakusana:");
        System.out.println("Haetaan...");
        List<Tip> byTitle = tipDao.getByValue(toMap("tip_name", keyword));
        List<Tip> byAuthor = tipDao.getByValue(toMap("author", keyword));
        System.out.println("Löytyi " + (byTitle.size() + byAuthor.size()) + " hakutulosta.");
        Stream.concat(byTitle.stream(), byAuthor.stream())
                .collect(Collectors.toList()).forEach(System.out::println);
        System.out.println("");
    }

    private void list() {
        System.out.println("Kaikki kirjat:");
        tipDao.list().forEach(System.out::println);
    }
}
