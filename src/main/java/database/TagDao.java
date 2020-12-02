package tietokanta;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import vinkkilogic.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static utilities.MappingUtils.keysToLowerUnderscore;
import static utilities.MappingUtils.toLowerUnderscore;

public class TagDao implements Dao<Tag, Long> {
    final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert sji;

    public TagDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("Tags").usingGeneratedKeyColumns("id");
    }

    @Override
    public Long create(Tag tag) {
        return sji.executeAndReturnKey(keysToLowerUnderscore(tag.getPropertyMap())).longValue();
    }

    @Override
    public Tag get(Long key) {
        return jdbcTemplate.query(
                "SELECT * FROM Tags WHERE id = ?",
                new BeanPropertyRowMapper<>(Tag.class),
                key
        ).get(0);
    }

    @Override
    public List<Tag> getByValue(Map<String, Object> map) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        return jdbcTemplate.query(
                "SELECT * FROM Tags WHERE "  + toLowerUnderscore(entry.getKey()) + " LIKE ?",
                new BeanPropertyRowMapper<>(Tag.class),
                "%" + entry.getValue() + "%"
        );
    }

    @Override
    public void update(Tag tag) {
        StringJoiner str = new StringJoiner(" = ?, ", "UPDATE Tags SET ", " = ? WHERE id = ?");
        Map<String, Object> map = tag.getPropertyMap();
        map.remove("id");
        map.remove("courses"); //NOT SAVED FOR NOW
        map.remove("tags"); //NOT SAVED FOR NOW
        ArrayList<Object> valueList = new ArrayList<>();
        map.forEach((k, v) -> {
            str.add(toLowerUnderscore(k));
            valueList.add(v);
        });
        valueList.add(tag.getId());
        jdbcTemplate.update(str.toString(), valueList.toArray());
    }

    @Override
    public void delete(Long key) {
        jdbcTemplate.update("DELETE FROM Tags WHERE id = ?", key);
    }

    @Override
    public void deleteByValue(Map<String, Object> map) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        jdbcTemplate.update(
                "DELETE FROM Tags WHERE " + toLowerUnderscore(entry.getKey()) + " LIKE ?",
                "%" + entry.getValue() + "%"
        );
    }

    @Override
    public List<Tag> list() {
        return jdbcTemplate.query(
                "SELECT * FROM Tags",
                new BeanPropertyRowMapper<>(Tag.class)
        );
    }

}
