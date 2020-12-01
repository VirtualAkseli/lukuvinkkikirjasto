package tietokanta;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import vinkkilogic.Tip;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static utilities.MappingUtils.keysToLowerUnderscore;
import static utilities.MappingUtils.toLowerUnderscore;


public class TipDao implements Dao<Tip, Long> {
    final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert sji;

    public TipDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("Tips").usingGeneratedKeyColumns("id");
    }

    @Override
    public Long create(Tip tip) {
        return sji.executeAndReturnKey(keysToLowerUnderscore(tip.getPropertyMap())).longValue();
    }

    @Override
    public Tip get(Long key) {
        return jdbcTemplate.query(
                "SELECT * FROM Tips WHERE id = ?",
                new BeanPropertyRowMapper<>(Tip.class),
                key
        ).get(0);
    }

    @Override
    public List<Tip> getByValue(Map<String, Object> map) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        return jdbcTemplate.query(
                "SELECT * FROM Tips WHERE "  + toLowerUnderscore(entry.getKey()) + " LIKE ?",
                new BeanPropertyRowMapper<>(Tip.class),
                "%" + entry.getValue() + "%"
        );
    }

    @Override
    public void update(Tip tip) {
        StringJoiner str = new StringJoiner(" = ?, ", "UPDATE Tips SET ", " = ? WHERE id = ?");
        Map<String, Object> map = tip.getPropertyMap();
        map.remove("id");
        map.remove("courses"); //NOT SAVED FOR NOW
        map.remove("tags"); //NOT SAVED FOR NOW
        ArrayList<Object> valueList = new ArrayList<>();
        map.forEach((k, v) -> {
            str.add(toLowerUnderscore(k));
            valueList.add(v);
        });
        valueList.add(tip.getId());
        jdbcTemplate.update(str.toString(), valueList.toArray());
    }

    @Override
    public void delete(Long key) {
        jdbcTemplate.update("DELETE FROM Tips WHERE id = ?", key);
    }

    @Override
    public void deleteByValue(Map<String, Object> map) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        jdbcTemplate.update(
                "DELETE FROM Tips WHERE " + toLowerUnderscore(entry.getKey()) + " LIKE ?",
                "%" + entry.getValue() + "%"
        );
    }

    @Override
    public List<Tip> list() {
        return jdbcTemplate.query(
                "SELECT * FROM Tips",
                new BeanPropertyRowMapper<>(Tip.class)
        );
    }

}