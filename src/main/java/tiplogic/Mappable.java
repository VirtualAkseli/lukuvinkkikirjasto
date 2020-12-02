package vinkkilogic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface Mappable {

    default Map<String, Object> getPropertyMap() {
        Map<String, Object> map = new HashMap<>();
        Arrays.stream(this.getClass().getDeclaredFields()).forEach((field -> {
            try { map.put(field.getName(), field.get(this)); }
            catch (IllegalAccessException e) { e.printStackTrace(); }
        }));
        return map;
    }

}
