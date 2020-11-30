package vinkkilogic;

import java.util.*;

public class Kirja implements Vinkki {
    private String nimi;
    private String kirjailija;
    private ArrayList<String> tagit;
    private ArrayList<String> data;
    private ArrayList<String> kurssit;
    private String kuvaus;
    
    public Kirja(String nimi, String kirjailija) {
        this.nimi = nimi;
        this.kirjailija = kirjailija;
        this.tagit = new ArrayList<>();
        this.data = new ArrayList<>();
        this.kurssit = new ArrayList<>();
    }

    @Override
    public String tyyppi() {
        return "Kirja";
    }

    @Override
    public String getNimi() {
        return this.nimi;
    }

    @Override
    public String getTekija() {
        return this.kirjailija;
    }

    @Override
    public List<String> getTagit() {
        return this.tagit;
    }

    @Override
    public void lisaaTagi(String t) {
        this.tagit.add(t);
    }

    @Override
    public List<String> getData() {
        return this.data;
    }

    @Override
    public void lisaaDataa(String d) {
        this.data.add(d);
    }

    @Override
    public List<String> getKurssit() {
        return this.kurssit;
    }

    @Override
    public void lisaaKurssi(String k) {
        this.kurssit.add(k);
    }

    @Override
    public String getUrl() {
        return "";
    }

    @Override
    public String getKuvaus() {
        return this.kuvaus;
    }

    @Override
    public void lisaaKuvaus(String k) {
        this.kuvaus = k;

    }
}
