package tietokanta;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import vinkkilogic.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

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
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        return jdbcTemplate.query(
                "SELECT * FROM Courses WHERE "  + toLowerUnderscore(entry.getKey()) + " LIKE ?",
                new BeanPropertyRowMapper<>(Course.class),
                "%" + entry.getValue() + "%"
        );
    }

    @Override
    public void update(Course course) {
        StringJoiner str = new StringJoiner(" = ?, ", "UPDATE Courses SET ", " = ? WHERE id = ?");
        Map<String, Object> map = course.getPropertyMap();
        map.remove("id");
        ArrayList<Object> valueList = new ArrayList<>();
        map.forEach((k, v) -> {
            str.add(toLowerUnderscore(k));
            valueList.add(v);
        });
        valueList.add(course.getId());
        jdbcTemplate.update(str.toString(), valueList.toArray());
    }

    @Override
    public void delete(Long key) {
        jdbcTemplate.update("DELETE FROM Courses WHERE id = ?", key);
    }

    @Override
    public void deleteByValue(Map<String, Object> map) {
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
