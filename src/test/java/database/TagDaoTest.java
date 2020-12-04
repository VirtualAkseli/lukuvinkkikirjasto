package database;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import tiplogic.Tag;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static utilities.MappingUtils.toMap;

public class TagDaoTest {
    private EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("tags.sql").build();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
    private TagDao tagDao = new TagDao(jdbcTemplate);

    @Before
    public void before() {
        jdbcTemplate.execute("TRUNCATE TABLE Tags; ALTER TABLE Tags ALTER COLUMN id RESTART WITH 1;");
    }

    @Test
    public void get() {
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ('name')");
        Tag out = tagDao.get(1L);
        assertThat(out.getTagName(), is("name"));
    }

    @Test
    public void getByValue() {
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ('name')");
        List<Tag> list = tagDao.getByValue(toMap("tagName", "name"));
        assertThat(list.size(), is(1));
        Tag out = list.get(0);
        assertThat(out.getTagName(), is("name"));
    }

    @Test
    public void update() {
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ( ? )", "old name");
        Tag in = new Tag();
        in.setId(1L);
        in.setTagName("name");
        tagDao.update(in);
        Tag out = jdbcTemplate.query("SELECT * FROM Tags WHERE id = 1",
                new BeanPropertyRowMapper<>(Tag.class)).get(0);
        assertThat(out.getTagName(), is("name"));
    }

    @Test
    public void delete() {
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ( ? )", "old name");;
        tagDao.delete(1L);
        List<Tag> out = jdbcTemplate.query("SELECT * FROM Tags WHERE id = 1",
                new BeanPropertyRowMapper<>(Tag.class));
        assertThat(out.size(), is(0));
    }

    @Test
    public void deleteByValue() {
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ('name')");
        tagDao.deleteByValue(toMap("tagName", "name"));
        List<Tag> out = jdbcTemplate.query("SELECT * FROM Tags WHERE id = 1",
                new BeanPropertyRowMapper<>(Tag.class));
        assertThat(out.size(), is(0));
    }

    @Test
    public void list() {
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ('1'), ('2'), ('3')");
        List<Tag> list = tagDao.list();
        Function<List<Tag>, Integer> getSum = (tags) ->
                tags.stream().map(Tag::getTagName).mapToInt(s ->
                        s != null && (s).matches("\\d+") ? Integer.parseInt(s) : 0).sum();

        assertThat(list.get(0).getId(), is(1L));
        assertThat(list.get(1).getId(), is(2L));
        assertThat(list.get(2).getId(), is(3L));
        assertThat(getSum.apply(list), is(IntStream.range(1, 4).sum()));
    }

}
