package tietokanta;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static utilities.MappingUtils.toMap;

import vinkkilogic.Tip;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class TipDaoTest {
    private EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("test_schema.sql").build();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
    private TipDao tipDao = new TipDao(jdbcTemplate);

    @Before
    public void before() {
        jdbcTemplate.execute(
                "SET REFERENTIAL_INTEGRITY FALSE;" +
                "TRUNCATE TABLE Tips; " +
                        "ALTER TABLE Tips ALTER COLUMN id RESTART WITH 1;" +
                "TRUNCATE TABLE Courses; " +
                        "ALTER TABLE Courses ALTER COLUMN id RESTART WITH 1;" +
                "TRUNCATE TABLE Tags; " +
                        "ALTER TABLE Tags ALTER COLUMN id RESTART WITH 1;" +
                "SET REFERENTIAL_INTEGRITY TRUE "
        );
    }

    @Test
    public void get() {
        jdbcTemplate.update(
                "INSERT INTO Tips (tip_type, author, tip_name, identifier, url, comments)" +
                        " VALUES ('book', 'author', 'name', 'identifier', 'url', 'comments' )"
        );
        Tip out = tipDao.get(1L);
        assertThat(out.getTipType(), is("book"));
        assertThat(out.getAuthor(), is("author"));
        assertThat(out.getTipName(), is("name"));
        assertThat(out.getIdentifier(), is("identifier"));
        assertThat(out.getUrl(), is("url"));
    }

    @Test
    public void getByValue() {
        jdbcTemplate.update(
                "INSERT INTO Tips (tip_type, author, tip_name, identifier, url, comments)" +
                        " VALUES ('book', 'author', 'name', 'identifier', 'url', 'comments' )"
        );
        List<Tip> list = tipDao.getByValue(toMap("tipType", "book"));
        assertThat(list.size(), is(1));
        Tip out = list.get(0);
        assertThat(out.getTipType(), is("book"));
        assertThat(out.getAuthor(), is("author"));
        assertThat(out.getTipName(), is("name"));
        assertThat(out.getIdentifier(), is("identifier"));
        assertThat(out.getUrl(), is("url"));
    }

    @Test
    public void update() {
        jdbcTemplate.update(
                "INSERT INTO Tips (tip_type, author, tip_name, identifier, url, comments)" +
                        " VALUES ( ?, ?, ?, ?, ?, ? )",
                "book", "old author", "old name", "identifier", "url", "comments"
        );
        Tip in = new Tip();
        in.setId(1L);
        in.setTipName("name");
        in.setAuthor("author");
        tipDao.update(in);
        Tip out = jdbcTemplate.query(
                "SELECT * FROM Tips WHERE id = 1",
                new BeanPropertyRowMapper<>(Tip.class)
        ).get(0);
        assertThat(out.getAuthor(), is("author"));
        assertThat(out.getTipName(), is("name"));
    }

    @Test
    public void delete() {
        jdbcTemplate.update(
                "INSERT INTO Tips (tip_type, author, tip_name, identifier, url, comments)" +
                        " VALUES ('book', 'author', 'name', 'identifier', 'url', 'comments' )"
        );
        tipDao.delete(1L);
        List<Tip> out = jdbcTemplate.query(
                "SELECT * FROM Tips WHERE id = 1",
                new BeanPropertyRowMapper<>(Tip.class)
        );
        assertThat(out.size(), is(0));
    }

    @Test
    public void deleteByValue() {
        jdbcTemplate.update(
                "INSERT INTO Tips (tip_type, author, tip_name, identifier, url, comments)" +
                        " VALUES ('book', 'author', 'name', 'identifier', 'url', 'comments' )"
        );
        tipDao.deleteByValue(toMap("tipName", "name"));
        List<Tip> out = jdbcTemplate.query(
                "SELECT * FROM Tips WHERE id = 1",
                new BeanPropertyRowMapper<>(Tip.class)
        );
        assertThat(out.size(), is(0));
    }

    @Test
    public void list() {
        jdbcTemplate.update(
                "INSERT INTO Tips (tip_type, author, tip_name, identifier, url, comments) VALUES" +
                        " ('book', '1', '2', '3', '4', '5' )," +
                        " ('video', '6', '7', '8', '9', '10' )," +
                        " ('blogpost', '11', '12', '13', '14', '15' )"
        );
        BiFunction<List<Tip>, Integer, Integer> getSumAt = (tips, position) ->
                tips.get(position).getPropertyMap().values().stream()
                        .mapToInt(s -> s instanceof String && ((String) s).matches("\\d+") ?
                                Integer.parseInt(String.valueOf(s)) : 0).sum();

        List<Tip> list = tipDao.list();
        assertThat(list.get(0).getId(), is(1L));
        assertThat(list.get(0).getTipType(), is("book"));
        assertThat(getSumAt.apply(list, 0), is(IntStream.range(1, 6).sum()));
        assertThat(list.get(1).getId(), is(2L));
        assertThat(list.get(1).getTipType(), is("video"));
        assertThat(getSumAt.apply(list, 1), is(IntStream.range(6, 11).sum()));
        assertThat(list.get(2).getId(), is(3L));
        assertThat(list.get(2).getTipType(), is("blogpost"));
        assertThat(getSumAt.apply(list, 2), is(IntStream.range(11, 16).sum()));
    }

}
