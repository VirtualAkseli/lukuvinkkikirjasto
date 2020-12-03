package utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class MappingUtils {

    /**
     * Luo annetuista argumenttipareista Map-olion. Argumenttien määrän tulee olla parillinen; ensimmäisestä
     * argumentista tulee avain, ja toisesta argumentista arvo avaimelle. Näin toimitaan jokaisen argumenttiparin
     * kohdalla.
     *
     * @param p "avain1", arvo1, "avain2", arvo2 ...
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static <V> Map<String, V> toMap(Object... p) {
        return IntStream.range(0, p.length / 2)
                .mapToObj(i -> new Object[]{p[i * 2], p[i * 2 + 1]})
                .filter((e) -> e[1] != null)
                .collect(Collectors.toMap(e -> String.valueOf(e[0]), e -> (V) e[1]));
    }

    /**
     * Luo annetusta Map-oliosta ja olioargumenttipareista uuden Map-olion sulauttamalla argumenttiparit annettuun
     * Map-olioon. Käyttää sisäisesti Map-rajapinnan merge()-metodia.
     *
     * @param map1 Map-olio, johon argumenttiparit sulautetaan
     * @param p "avain1", arvo1, "avain2", arvo2 ...
     * @return Map
     */
    public static <V> Map<String, V> toMap(Map<String, V> map1, Object... p) {
        Map<String, V> map2 = toMap(p);
        map2.forEach((k, v) -> map1.merge(k, v, (v1, v2) -> v2));
        return map1;
    }

    /**
     * Muuntaa annetun merkkijonon muodosta camelCase muotoon camel_case.
     *
     * @param string Muunnettava merkkijono
     * @return Muunnettu merkkijono
     */
    public static String toLowerUnderscore(String string) {
        return string.replaceAll("([a-z\\d])([A-Z\\d])", "$1_$2").toLowerCase();
    }

    /**
     * Luo annetusta Map-oliosta uuden Map-olion, jonka merkkijonomuotoiset avaimet on muunnettu muodosta camelCase
     * muotoon camel_case.
     *
     * @param map Map-olio, josta uusi olio muodostetaan
     * @return Uusi Map-olio
     */
    public static Map<String, Object> keysToLowerUnderscore(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<>();
        map.forEach((k, v) -> newMap.put(toLowerUnderscore(k), v));
        return newMap;
    }


}
