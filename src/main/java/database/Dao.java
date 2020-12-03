package database;

import tiplogic.Mappable;

import java.util.*;

import static utilities.MappingUtils.toLowerUnderscore;
import static utilities.MappingUtils.toMap;

public interface Dao<T, K> {

    K create(T object);

    T get(K key);

    /**
     * Get all objects with property "someProperty" set to value "someValue":<br>
     * dao.getByValue(toMap("someProperty", "someValue"))
     *
     * @param map a map (size should be 1) instance that associates some key (property name) with some value
     * @return list of matching objects
     */
    List<T> getByValue(Map<String, Object> map);

    List<T> getByValue(Map<String, Object> map, Boolean exactMatch);

    void update(T object);

    void delete(K key);

    void deleteByValue(Map<String, Object> map);

    void deleteByValue(Map<String, Object> map, Boolean exactMatch);

    List<T> list();

    default Map<String, ?> getUpdateMap(String tableName, Mappable mappable) {
        return getUpdateMap(tableName, mappable, new String[0]);
    }

    default Map<String, ?> getUpdateMap(String tableName, Mappable mappable, String[] excluded) {
        StringJoiner str = new StringJoiner(
                " = ?, ", "UPDATE " + tableName + " SET ", " = ? WHERE id = ?");
        Map<String, Object> map = mappable.getPropertyMapWithoutId();
        ArrayList<Object> valueList = new ArrayList<>();
        map.forEach((k, v) -> {
            if (!Arrays.asList(excluded).contains(k)) {
                str.add(toLowerUnderscore(k));
                valueList.add(v);
            }
        });
        valueList.add(mappable.getId());
        return toMap("queryString", str.toString(), "valueList", valueList.toArray());
    }

}