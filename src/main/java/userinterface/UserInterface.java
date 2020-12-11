package userinterface;

import io.IO;
import tiplogic.Tip;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import controller.TipController;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import tiplogic.Tag;

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
        io.print("3. Listaa tallennetut lukuvinkit - muokkaa tallennettuja lukuvinkkejä");
        io.print("4. Lopeta");
        io.print("");
    }

    private void add() {
        String[] tipData = new String[8];
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
       System.out.println("Lisätty!");
    }

    private void listTypes() {
        io.print("");
        io.print("1. Kirja");
        io.print("2. Podcast");
        io.print("3. Blogi");
        io.print("4. Video"); 
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
            case "4":
                return "video";
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
            io.print("\n\n1. Kyllä");
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
        io.print("\n\n1. Kyllä");
        io.print("2. Ei");
        String command = io.readLine("Tallennetaanko vinkki näillä tiedoilla?");
        if (command.equals("1")) {
            return tipData;
        } else {
            return getInput(tipData);
        }
    }

    public String[] getMandatoryInfo(String[] tipData) {
        io.print("\n\nVastaathan kaikkiin kohtiin.");
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
            case "video":
                tipData[2] = io.readLine("Videon nimi:");
                tipData[4] = io.readLine("Videon URL:");
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
                tipData = getTags(tipData);
                tipData = getCourses(tipData);
                return tipData;
            case "podcast":
                tipData[1] = io.readLine("Podcastin tekijä:");
                tipData[3] = io.readLine("Jakson nimi:");
                tipData = getTags(tipData);
                tipData = getCourses(tipData);
                return tipData;
            case "blogpost":
                tipData[1] = io.readLine("Blogin pitäjä:");
                tipData[5] = io.readLine("Kommentti:");
                tipData = getTags(tipData);
                tipData = getCourses(tipData);
                return tipData;
            case "video":
                tipData[1] = io.readLine("Videon tekijä:");
                tipData[5] = io.readLine("Kommentti:");
                tipData = getTags(tipData);
                tipData = getCourses(tipData);
                return tipData;
            default:
                return tipData;
        }
    }
    
        private String[] getTags(String[] tipData){
       
            io.print("\n\n1. Syötä tagi");
            io.print("2. Älä lisää tagia");
            String command = io.readLine("Haluatko merkata lukuvinkin tagilla?");
            switch(command){
                case"1":
                    String tag = io.readLine("Anna tagin nimi");
                    tipData[6] = tipData[6] + tag + ",";
                    while(true){
                        io.print("\n\n1. Kyllä - Syötä toinen tagi");
                        io.print("2. En");
                        command = io.readLine("Haluatko antaa toisen tagin?");
                        if (command.equals("1")) {
                            tipData[6] = tipData[6] + io.readLine("Anna tagin nimi") + ",";
                        }
                        if (command.equals("2"))
                            break;
                    }
                    return tipData;
                case"2":
                    return tipData;
                default:
                    return getTags(tipData);
            }
    }
    
    private String[] getCourses(String[] tipData){
       
            io.print("\n\n1. Lisää kurssi");
            io.print("2. Älä lisää kurssia");
            String command = io.readLine("Haluatko lisätä lukuvinkkiin liittyvän kurssin?");
            switch(command){
                case"1":
                    String course = io.readLine("Anna kurssin nimi");
                    tipData[7] = tipData[7] + course + ",";
                    while(true){
                        io.print("\n\n1. Kyllä - Lisää toinen kurssi");
                        io.print("2. En");
                        command = io.readLine("Haluatko lisätä toisen kurssin?");
                        if(command.equals("1")) tipData[7] = tipData[7] + io.readLine("Anna kurssin nimi") + ",";
                        if(command.equals("2")) break;
                    }
                    return tipData;
                case"2":
                    return tipData;
                default:
                    return getCourses(tipData);
            }
    }

     private void search() {
            io.print("\n\n1. Hae vinkin nimen tai tekijän perusteella.");
            io.print("2. Hae tagin perusteella");
            io.print("3. Peruuta");
            String command = io.readLine("Valitse numerolla sopiva haku");
            AtomicInteger positionInList = new AtomicInteger(1);
            switch(command){
                case"1":
                    String keyword = io.readLine("Hakusana:");
                    io.print("Haetaan...");
                    List<Tip> results = controller.searchWithWord(keyword);
                    io.print("Löytyi " + results.size() + " hakutulosta.");
                    results.stream().collect(Collectors.toList()).forEach(item -> io.print(item.toString()));
                    io.print("");
                    return;
                case"2":
                    io.print("Tallennetut tagit:");
                    List<Tag> savedTags = controller.listTags();
                    for(Tag x : savedTags){
                        io.print(positionInList.getAndIncrement() +". " + x.getTagName());
                    }
                    
                    while(true){
                           String targetTagString = io.readLine("Valitse haettava tagi numerolla");
                           try {
                               int targetTag = Integer.valueOf(targetTagString);
                               if(targetTag < positionInList.get() && targetTag >0){
                                   results = controller.searchWithTag(savedTags.get(targetTag-1));
                                   break;
                               }
                               
                           } catch(Exception e) {
                               io.print("Virheellinen valinta");
                           }
                       }
                    io.print("Haetaan...");
                    io.print("Löytyi " + results.size() + " hakutulosta.");
                    results.stream().collect(Collectors.toList()).forEach(item -> io.print(item.toString()));
                    io.print("");
                    return;
                case"3":
                    return;
                default:
                    search();
            }  
         }

    
    private void list() {
        io.print("\n\n1. Kirjat");
        io.print("2. Podcastit");
        io.print("3. Blogit");
        io.print("4. Videot");
        io.print("5. Kaikki");
        io.print("6. Peruuta listaus");
        String command = io.readLine("Valitse listattavat lukuvinkit");
        List<Tip> searchResult = null;
        AtomicInteger positionInList = new AtomicInteger(1);
            switch(command){
                case "1":
                    io.print("Tallennetut kirjat:");
                    searchResult = controller.searchWithType("book");
                    break;
                case "2":
                    io.print("Tallennetut podcastit:");
                    searchResult = controller.searchWithType("podcast");
                    break;
                case "3":
                    io.print("Tallennetut blogit:");
                    searchResult = controller.searchWithType("blogpost");
                    break;
                case "4":
                    io.print("Tallennetut videot:");
                    searchResult = controller.searchWithType("video");
                    break;
                case "5":
                    io.print("Kaikki lukuvinkit:");
                    searchResult = controller.list();
                    break;
                case "6":
                    return;
                default:
                    list();     
            }
             for(Tip tip : searchResult){
                        if(tip.toString().contains("[LUETTU")){
                            io.print("\n[LUETTU] Vinkin numero: " + positionInList.getAndIncrement() + ".[LUETTU] \n" + tip.toString().replace("[LUETTU]",""));
                        }else {
                            io.print("\nVinkin numero: " + positionInList.getAndIncrement() + ". \n" + tip.toString());
                        }
                    }
            
            if(userWantsToModify()){
               switch(whatToModify()){
                   case"edit":
                       while(true){
                           String modifyTarget = io.readLine("\nValitse muokattava vinkki listan numerolla.");
                           try {
                               int targetIndex = Integer.valueOf(modifyTarget);
                               if(targetIndex < positionInList.get() && targetIndex >0){
                                   modifyTip(searchResult.get(targetIndex-1));
                                   break;
                               }
                               
                           } catch(Exception e) {
                               io.print("Virheellinen valinta");
                           }
                       }
                       return;
                   case"markAsRead":
                       while(true){
                           String modifyTarget = io.readLine("\nValitse luetuksi merkattava vinkki numerolla, uudelleen merkkaus poistaa merkinnän.");
                           try {
                               int targetIndex = Integer.valueOf(modifyTarget);
                               if(targetIndex < positionInList.get() && targetIndex >0){
                                   controller.markTipAsRead(searchResult.get(targetIndex-1));
                                   break;
                               }
                           } catch(Exception e) {
                               io.print("Virheellinen valinta");
                           }
                       }
                       return;
                   case"":
                       
               }
            }
    }
    
    private void modifyTip(Tip tip){
        io.print("\nValitsit lukuvinkin:\n" + tip.toString().replace("[LUETTU]", ""));
        io.print("1. Poista vinkki "
                + "\n2. Muokkaa vinkin tietoja "
                + "\n3. Peruuta");
        String command = io.readLine("Valitse sopiva komento");
        
        switch(command){
            case"1":
                io.print("Poistetaan vinkki " + tip.getTipName().replace("[LUETTU]", ""));
                controller.deleteTip(tip);
                return;
            case "2":
                io.print("\nJätä vastaus tyhjäksi kohdista joita et halua muokata");
                boolean wasMarkedRead = tip.getTipName().contains("[LUETTU]");
                String author = io.readLine("Anna tekijän nimi:");
                String tipName = io.readLine("Anna teoksen nimi:");
                String url = io.readLine("Anna URL:");
                String identifier = io.readLine("Anna tunniste, kuten ISBN-numero tai podcast jakson nimi:");
                String comment = io.readLine("Lisää kommentti:");

                tip.setAuthor(author.equals("") ? tip.getAuthor() : author);
                tip.setTipName(tipName.equals("") ? tip.getTipName() : tipName);
                tip.setUrl(url.equals("") ? tip.getUrl() : url);
                tip.setIdentifier(identifier.equals("") ? tip.getIdentifier() : identifier);
                tip.setComments(comment.equals("") ? tip.getComments() : comment);
                if (wasMarkedRead) {
                    tip.setTipName("[LUETTU]" + tip.getTipName());
                }
                io.print("Muokattu vinkki:");
                io.print(tip.toString().replace("[LUETTU]", ""));
                controller.updateTip(tip);
                return;
            case"3":
                return;
            default:
                modifyTip(tip);
        }
       
    }
    
    private boolean userWantsToModify(){
        io.print("\n\n1. Kyllä");
        io.print("2. En");
        String command = io.readLine("Haluatko muokata tai merkata luetuksi?");
        switch(command){
            case "1":
                return true;
            case "2":
                return false;
            default:
                return userWantsToModify();
        }
    }
    
    private String whatToModify(){
        io.print("\n\n1. Muokkaa vinkin tietoja / poista vinkki");
        io.print("2. Merkkaa vinkki luetuksi / poista merkkaus");
        io.print("3. Peruuta");
        String command = io.readLine("Haluatko muokata tai merkata luetuksi?");
        switch(command){
            case"1":
                return "edit";
            case"2":
                return "markAsRead";
            case"3":
                return "";
            default:
                return whatToModify();
        }
    }
}