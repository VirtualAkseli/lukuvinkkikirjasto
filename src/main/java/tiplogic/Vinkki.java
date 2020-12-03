package tiplogic;

import java.util.List;

public interface Vinkki {
    String tyyppi();
    String getNimi();               //podcastilla jakson nimi
    String getTekija();             //podcastilla podcastin nimi
    String getUrl();
    List<String> getTagit();   //tagit joilla vinkkejä voi hakea
    List<String> getData();    //muu data, kuten kirjojen ISBN
    List<String> getKurssit(); //vinkkiin liittyvät kurssit
    String getKuvaus();
    void lisaaTagi(String t);
    void lisaaDataa(String d);
    void lisaaKurssi(String k);
    void lisaaKuvaus(String k);
}
