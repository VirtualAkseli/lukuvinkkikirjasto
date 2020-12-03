package tietokanta;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;
import vinkkilogic.Course;
import vinkkilogic.Mappable;
import vinkkilogic.Tag;
import vinkkilogic.Tip;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static utilities.MappingUtils.*;


public class TipDao implements Dao<Tip, Long> {
    final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert sji;
    private final SimpleJdbcInsert sjiTipCourses;
    private final SimpleJdbcInsert sjiTipTags;

    public TipDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("Tips").usingGeneratedKeyColumns("id");
        this.sjiTipCourses = new SimpleJdbcInsert(jdbcTemplate).withTableName("Tip_courses");
        this.sjiTipTags = new SimpleJdbcInsert(jdbcTemplate).withTableName("Tip_tags");
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?>[] getMaps(List<? extends Mappable> list, Function<Mappable, Map<String, Object>> func) {
        return list.stream().map(func).toArray(Map[]::new);
    }

    protected void saveListProperties(Tip tip) {
        sjiTipCourses.executeBatch(getMaps(tip.getCourses(), c ->
                toMap("course_id", c.getId(), "tip_id", tip.getId())));
        sjiTipTags.executeBatch(getMaps(tip.getTags(), t ->
                toMap("tag_id", t.getId(), "tip_id", tip.getId())));
    }

    public List<Course> getTipCourses(Long id) {
        return jdbcTemplate.query(
                "SELECT c.* FROM Tip_courses tc JOIN Courses c on tc.course_id = c.id WHERE tip_id = ? ",
                new BeanPropertyRowMapper<>(Course.class), id
        );
    }

    public List<Tag> getTipTags(Long id) {
        return jdbcTemplate.query(
                "SELECT t.* FROM Tip_tags tc JOIN Tags t on tc.tag_id = t.id WHERE tip_id = ? ",
                new BeanPropertyRowMapper<>(Tag.class), id
        );
    }

    public void deleteTipTags(Long id) {
        jdbcTemplate.update("DELETE FROM Tip_tags WHERE tip_id = ?", id);
    }

    public void deleteTipCourses(Long id) {
        jdbcTemplate.update("DELETE FROM Tip_courses WHERE tip_id = ?", id);
    }


    @Transactional
    @Override
    public Long create(Tip tip) {
        Long id = sji.executeAndReturnKey(keysToLowerUnderscore(tip.getPropertyMap())).longValue();
        tip.setId(id);
        saveListProperties(tip);
        return id;
    }

    @Override
    public Tip get(Long key) {
        Tip tip = jdbcTemplate.query(
                "SELECT * FROM Tips WHERE id = ?",
                new BeanPropertyRowMapper<>(Tip.class),
                key
        ).get(0);
        tip.setCourses(getTipCourses(tip.getId()));
        tip.setTags(getTipTags(tip.getId()));
        return tip;
    }

    @Override
    public List<Tip> getByValue(Map<String, Object> map) {
        return getByValue(map, false);
    }

    @Override
    public List<Tip> getByValue(Map<String, Object> map, Boolean exactMatch) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        List<Tip> tip = jdbcTemplate.query(
                "SELECT * FROM Tips WHERE "  + toLowerUnderscore(entry.getKey()) + " LIKE ?",
                new BeanPropertyRowMapper<>(Tip.class),
                exactMatch ? "%" + entry.getValue() + "%" : entry.getValue()
        );
        return tip.stream().peek((t) -> {
            t.setCourses(getTipCourses(t.getId()));
            t.setTags(getTipTags(t.getId()));
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void update(Tip tip) {
        Map<String, ?> map = getUpdateMap("Tips", tip, new String[]{"courses", "tags"});
        jdbcTemplate.update((String) map.get("queryString"), (Object[]) map.get("valueList"));
        deleteTipCourses(tip.getId());
        deleteTipTags(tip.getId());
        saveListProperties(tip);
    }

    @Override
    public void delete(Long key) {
        jdbcTemplate.update("DELETE FROM Tips WHERE id = ?", key);
    }

    @Override
    public void deleteByValue(Map<String, Object> map) {
        deleteByValue(map, false);
    }

    @Override
    public void deleteByValue(Map<String, Object> map, Boolean exactMatch) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        jdbcTemplate.update(
                "DELETE FROM Tips WHERE " + toLowerUnderscore(entry.getKey()) + " LIKE ?",
                "%" + entry.getValue() + "%"
        );
    }

    @Override
    public List<Tip> list() {
        List<Tip> tips = jdbcTemplate.query("SELECT * FROM Tips", new BeanPropertyRowMapper<>(Tip.class));
        tips.forEach(tip -> {
            tip.setCourses(getTipCourses(tip.getId()));
            tip.setTags(getTipTags(tip.getId()));
        });
        return tips;
    }

}