package tiplogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class KirjaTest {
    private Kirja kirja;

    @Before
    public void setUp(){
        this.kirja = new Kirja("Rumo", "Walter Moers");
    }

    @Test
    public void nimiOikein() {
        assertEquals("Rumo", kirja.getNimi());
    }

    @Test
    public void kirjailijaOikein() {
        assertEquals("Walter Moers", kirja.getTekija());
    }

    @Test
    public void tagitToimii() {
        kirja.lisaaTagi("fantasia");
        assertTrue(kirja.getTagit().contains("fantasia"));
        assertEquals(1, kirja.getTagit().size());
        kirja.lisaaTagi("nuorten");
        assertTrue(kirja.getTagit().contains("nuorten"));
        assertEquals(2, kirja.getTagit().size());
    }

    @Test
    public void dataLisataan() {
        kirja.lisaaDataa("ISBN: 951-1-19584-0");
        assertTrue(kirja.getData().contains("ISBN: 951-1-19584-0"));
        assertEquals(1, kirja.getData().size());
        kirja.lisaaDataa("Kuvaus: :)");
        assertTrue(kirja.getData().contains("Kuvaus: :)"));
        assertEquals(2, kirja.getData().size());
    }

    @Test
    public void kurssitLisataan() {
        kirja.lisaaKurssi("OHTU");
        assertTrue(kirja.getKurssit().contains("OHTU"));
        assertEquals(1, kirja.getKurssit().size());
        kirja.lisaaKurssi("TIKAPE");
        assertTrue(kirja.getKurssit().contains("TIKAPE"));
        assertEquals(2, kirja.getKurssit().size());
    }
}
