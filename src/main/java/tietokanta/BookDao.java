package tietokanta;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import vinkkilogic.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static utilities.MappingUtils.keysToLowerUnderscore;
import static utilities.MappingUtils.toLowerUnderscore;


public class BookDao implements Dao<Book, Long> {
    final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert sji;

    public BookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sji = new SimpleJdbcInsert(jdbcTemplate).withTableName("Books").usingGeneratedKeyColumns("id");
    }

    @Override
    public Long create(Book book) {
        return sji.executeAndReturnKey(keysToLowerUnderscore(book.getPropertyMap())).longValue();
    }

    @Override
    public Book get(Long key) {
        return jdbcTemplate.query(
                "SELECT * FROM Books WHERE id = ?",
                new BeanPropertyRowMapper<>(Book.class),
                key
        ).get(0);
    }

    @Override
    public List<Book> getByValue(Map<String, Object> map) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        return jdbcTemplate.query(
                "SELECT * FROM Books WHERE "  + toLowerUnderscore(entry.getKey()) + " LIKE ?",
                new BeanPropertyRowMapper<>(Book.class),
                "%" + entry.getValue() + "%"
        );
    }

    @Override
    public void update(Book book) {
        StringJoiner str = new StringJoiner(" = ?, ", "UPDATE Books SET ", " = ? WHERE id = ?");
        Map<String, Object> map = book.getPropertyMap();
        map.remove("id");
        ArrayList<Object> valueList = new ArrayList<>();
        map.forEach((k, v) -> {
            str.add(k);
            valueList.add(v);
        });
        valueList.add(book.getId());
        jdbcTemplate.update(str.toString(), valueList.toArray());
    }

    @Override
    public void delete(Long key) {
        jdbcTemplate.update("DELETE FROM Books WHERE id = ?", key);
    }

    @Override
    public void deleteByValue(Map<String, Object> map) {
        Map.Entry<String, Object> entry = map.entrySet().iterator().next();
        jdbcTemplate.update(
                "DELETE FROM Books WHERE " + toLowerUnderscore(entry.getKey()) + " LIKE ?",
                "%" + entry.getValue() + "%"
        );
    }

    @Override
    public List<Book> list() {
        return jdbcTemplate.query(
                "SELECT * FROM Books",
                new BeanPropertyRowMapper<>(Book.class)
        );
    }

}