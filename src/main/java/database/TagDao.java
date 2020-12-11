package database;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import tiplogic.Tag;

import java.util.List;
import java.util.Map;

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
        return getByValue(map, false);
    }

    @Override
    public List<Tag> getByValue(Map<String, Object> map, Boolean exactMatch) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        return jdbcTemplate.query(
                "SELECT * FROM Tags WHERE "  + toLowerUnderscore(entry.getKey()) + " LIKE ?",
                new BeanPropertyRowMapper<>(Tag.class),
                exactMatch ? "%" + entry.getValue() + "%" : entry.getValue()
        );
    }
    
 
    public List<Tag> getTagsInUse(){
        String sql ="SELECT * FROM Tags WHERE exists(SELECT 1 FROM Tip_tags where tag_id = id)";
        List<Tag> result = jdbcTemplate.query(
            sql, new BeanPropertyRowMapper(Tag.class));
        return result;
    }

    @Override
    public void update(Tag tag) {
        Map<String, ?> map = getUpdateMap("Tags", tag);
        jdbcTemplate.update((String) map.get("queryString"), (Object[]) map.get("valueList"));
    }

    @Override
    public void delete(Long key) {
        jdbcTemplate.update("DELETE FROM Tags WHERE id = ?", key);
    }

    @Override
    public void deleteByValue(Map<String, Object> map) {
        deleteByValue(map, false);
    }

    @Override
    public void deleteByValue(Map<String, Object> map, Boolean exactMatch) {
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
