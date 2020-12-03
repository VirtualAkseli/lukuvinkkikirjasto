package tietokanta;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import vinkkilogic.Course;

import java.util.List;
import java.util.Map;

import static utilities.MappingUtils.keysToLowerUnderscore;
import static utilities.MappingUtils.toLowerUnderscore;

public class CourseDao implements Dao<Course, Long>{
    final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert sji;

    public CourseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("Courses").usingGeneratedKeyColumns("id");
    }

    @Override
    public Long create(Course course) {
        return sji.executeAndReturnKey(keysToLowerUnderscore(course.getPropertyMap())).longValue();
    }

    @Override
    public Course get(Long key) {
        return jdbcTemplate.query(
                "SELECT * FROM Courses WHERE id = ?",
                new BeanPropertyRowMapper<>(Course.class),
                key
        ).get(0);
    }

    @Override
    public List<Course> getByValue(Map<String, Object> map) {
        return getByValue(map, false);
    }

    @Override
    public List<Course> getByValue(Map<String, Object> map, Boolean exactMatch) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        return jdbcTemplate.query(
                "SELECT * FROM Courses WHERE "  + toLowerUnderscore(entry.getKey()) + " LIKE ?",
                new BeanPropertyRowMapper<>(Course.class),
                exactMatch ? "%" + entry.getValue() + "%" : entry.getValue()
        );
    }

    @Override
    public void update(Course course) {
        Map<String, ?> map = getUpdateMap("Courses", course);
        jdbcTemplate.update((String) map.get("queryString"), (Object[]) map.get("valueList"));
    }

    @Override
    public void delete(Long key) {
        jdbcTemplate.update("DELETE FROM Courses WHERE id = ?", key);
    }

    @Override
    public void deleteByValue(Map<String, Object> map) {
        deleteByValue(map, false);
    }

    @Override
    public void deleteByValue(Map<String, Object> map, Boolean exactMatch) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        jdbcTemplate.update(
                "DELETE FROM Courses WHERE " + toLowerUnderscore(entry.getKey()) + " LIKE ?",
                "%" + entry.getValue() + "%"
        );
    }

    @Override
    public List<Course> list() {
        return jdbcTemplate.query(
                "SELECT * FROM Courses",
                new BeanPropertyRowMapper<>(Course.class)
        );
    }

}
