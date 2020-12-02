package controller;

import java.util.*;
import tiplogic.*;

public class TipController {
    private ArrayList<Vinkki> tips;

    public TipController() {
        this.tips = new ArrayList<>();
    }
    //metodille annetaan taulukko jossa käyttäjältä pyydetty vinkin data
    //metodi olettaa että data taulukko on luotu käyttöliittymässä oikein
    public void addTip(String[] data) {
        if (data[0].equals("kirja")) addBook(data);  //ensimmäinen indeksi kertoo minkälainen vinkki luodaan
    }

    //stringit indekseissä: 1 kirjan nimi, 2 kirjailija, 3 tagit, 4 relevantit kurssit, 5 muu data
    private void addBook(String[] data) {
        Kirja book = new Kirja(data[1], data[2]);
        for (String tag: separate(data[3])) book.lisaaTagi(tag);
        for (String course: separate(data[4])) book.lisaaKurssi(course);
        for (String info: separate(data[5])) book.lisaaDataa(info);
        tips.add(book);                              //lisätään luotu kirja vinkkilistalle
    }

    public List<Vinkki> getTips() {
        return this.tips;
    }

    //metodi joka listaa vain tietyn tyyppiset vinkit

    public List<Vinkki> searchWithTag(String tag) {
        ArrayList<Vinkki> results = new ArrayList<>();
        for (Vinkki tip: tips) if (tip.getTagit().contains(tag)) results.add(tip);
        return results;
    }

    //apumetodi, joka pilkkoo annetun stringin Arrayhin merkkien | kohdalta
    private List<String> separate(String dataArrayList) {
        ArrayList<String> data = new ArrayList<>();
        if (!dataArrayList.contains("|")) {
            data.add(dataArrayList);
            return data;
        }
        String[] separatedStrings = dataArrayList.split("|");
        for (String pieceOfData: separatedStrings) data.add(pieceOfData.trim());
        return data;
    }
}
