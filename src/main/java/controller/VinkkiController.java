package controller;

import java.util.*;
import vinkkilogic.*;

public class VinkkiController {
    private ArrayList<Vinkki> vinkit;

    public VinkkiController() {
        this.vinkit = new ArrayList<>();
    }
    //metodille annetaan taulukko jossa käyttäjältä pyydetty vinkin data
    //metodi olettaa että tiedot taulukko on luotu käyttöliittymässä oikein
    public void lisaaVinkki(String[] tiedot) {
        if (tiedot[0].equals("kirja")) lisaaKirja(tiedot);  //ensimmäinen indeksi kertoo minkälainen vinkki luodaan
    }

    //stringit indekseissä: 1 kirjan nimi, 2 kirjailija, 3 tagit, 4 relevantit kurssit, 5 muu data
    private void lisaaKirja(String[] tiedot) {
        Kirja kirja = new Kirja(tiedot[1], tiedot[2]);
        for (String tag: erottele(tiedot[3])) kirja.lisaaTagi(tag);
        for (String kurssi: erottele(tiedot[4])) kirja.lisaaKurssi(kurssi);
        for (String data: erottele(tiedot[5])) kirja.lisaaDataa(data);
        vinkit.add(kirja);                              //lisätään luotu kirja vinkkilistalle
    }

    public List<Vinkki> getVinkit() {
        return this.vinkit;
    }

    //metodi joka listaa vain tietyn tyyppiset vinkit

    public List<Vinkki> haeTagilla(String tag) {
        ArrayList<Vinkki> haetut = new ArrayList<>();
        for (Vinkki v: vinkit) if (v.getTagit().contains(tag)) haetut.add(v);
        return haetut;
    }

    //apumetodi, joka pilkkoo annetun stringin Arrayhin merkkien | kohdalta
    private List<String> erottele(String s) {
        ArrayList<String> data = new ArrayList<>();
        if (!s.contains("|")) {
            data.add(s);
            return data;
        }
        String[] ero = s.split("|");
        for (String e: ero) data.add(e.trim());
        return data;
    }
}
