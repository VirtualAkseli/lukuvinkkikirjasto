package database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import tiplogic.Course;
import tiplogic.Tag;
import tiplogic.Tip;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static utilities.MappingUtils.toMap;

public class TipDaoJoinTablesTest {
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
                "TRUNCATE TABLE Tip_tags;" +
                "TRUNCATE TABLE Tip_courses;" +
                "SET REFERENTIAL_INTEGRITY TRUE "
        );
    }

    @After
    public void after() {
        db.shutdown();
    }

    @Test
    public void coursesAndTagsAreCreatedCorrectly() {
        Tip tip = new Tip("book");
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ('1'), ('5'), ('10'), ('20')");
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ('2'), ('6'), ('8'), ('10')");
        tip.setCourses(Arrays.asList(new Course(1L, ""), new Course(2L, ""), new Course(3L, "")));
        tip.setTags(Arrays.asList(new Tag(1L, ""), new Tag(2L, ""), new Tag(4L, "")));
        tipDao.create(tip);
        List<Map<String, Object>> courseMaps = jdbcTemplate.queryForList(
                "SELECT c.course_name FROM Tip_courses JOIN courses c on tip_courses.course_id = c.id");
        List<Map<String, Object>> tagMaps = jdbcTemplate.queryForList(
                "SELECT t.tag_name FROM Tip_tags JOIN tags t on tip_tags.tag_id = t.id");
        assertThat(courseMaps.stream().mapToInt(m -> Integer.parseInt((String) m.get("course_name"))).sum(), is(16));
        assertThat(tagMaps.stream().mapToInt(m -> Integer.parseInt((String) m.get("tag_name"))).sum(), is(18));
    }

    @Test
    public void coursesAndTagsAreReturnedCorrectlyWhenCallingGet() {
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ('course')");
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ('tag')");
        jdbcTemplate.update("INSERT INTO Tips (tip_type) VALUES ('book')");
        jdbcTemplate.update("INSERT INTO Tip_courses (tip_id, course_id) VALUES ('1', '1')");
        jdbcTemplate.update("INSERT INTO Tip_tags (tip_id, tag_id) VALUES ('1', '1')");
        Tip tip = tipDao.get(1L);
        List<Course> courses = tip.getCourses();
        List<Tag> tags = tip.getTags();
        assertThat(courses.get(0).getCourseName(), is("course"));
        assertThat(tags.get(0).getTagName(), is("tag"));
    }

    @Test
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public void coursesAndTagsAreUpdatedCorrectly() {
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ('old course')");
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ('course')");
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ('old tag')");
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ('tag')");
        jdbcTemplate.update("INSERT INTO Tips (tip_type) VALUES ('book')");
        jdbcTemplate.update("INSERT INTO Tip_courses (tip_id, course_id) VALUES ('1', '1')");
        jdbcTemplate.update("INSERT INTO Tip_tags (tip_id, tag_id) VALUES ('1', '1')");
        Tip tip = new Tip("name", "author");
        tip.setId(1L);
        tip.setTipType("book");
        tip.setCourses(Arrays.asList(new Course(2L, "")));
        tip.setTags(Arrays.asList(new Tag(2L, "")));
        tipDao.update(tip);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(
                "SELECT c.course_name FROM Tip_courses JOIN courses c on tip_courses.course_id = c.id WHERE tip_id = 1");
        assertThat(maps.size(), is(1));
        assertThat(maps.get(0).get("course_name"), is("course"));
        maps = jdbcTemplate.queryForList(
                "SELECT t.tag_name FROM Tip_tags JOIN tags t on tip_tags.tag_id = t.id WHERE tip_id = 1");
        assertThat(maps.size(), is(1));
        assertThat(maps.get(0).get("tag_name"), is("tag"));
    }

    @Test
    public void coursesAndTagsAreDeletedWhenTipsIsDeleted() {
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ('course')");
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ('tag')");
        jdbcTemplate.update("INSERT INTO Tips (tip_type) VALUES ('book')");
        jdbcTemplate.update("INSERT INTO Tips (tip_type) VALUES ('video')");
        jdbcTemplate.update("INSERT INTO Tip_courses (tip_id, course_id) VALUES ('1', '1')");
        jdbcTemplate.update("INSERT INTO Tip_courses (tip_id, course_id) VALUES ('2', '1')");
        jdbcTemplate.update("INSERT INTO Tip_tags (tip_id, tag_id) VALUES ('1', '1')");
        jdbcTemplate.update("INSERT INTO Tip_tags (tip_id, tag_id) VALUES ('2', '1')");
        tipDao.delete(1L);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(
                "SELECT c.course_name FROM Tip_courses JOIN courses c on tip_courses.course_id = c.id WHERE tip_id = 1");
        assertThat(maps.size(), is(0));
        maps = jdbcTemplate.queryForList(
                "SELECT t.tag_name FROM Tip_tags JOIN tags t on tip_tags.tag_id = t.id WHERE tip_id = 1");
        assertThat(maps.size(), is(0));
        maps = jdbcTemplate.queryForList(
                "SELECT c.course_name FROM Tip_courses JOIN courses c on tip_courses.course_id = c.id WHERE tip_id = 2");
        assertThat(maps.size(), is(1));
        maps = jdbcTemplate.queryForList(
                "SELECT t.tag_name FROM Tip_tags JOIN tags t on tip_tags.tag_id = t.id WHERE tip_id = 2");
        assertThat(maps.size(), is(1));
    }

    @Test
    public void deleteByValueDeletesCoursesAndTags() {
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ('course')");
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ('tag')");
        jdbcTemplate.update("INSERT INTO Tips (tip_type) VALUES ('book')");
        jdbcTemplate.update("INSERT INTO Tips (tip_type) VALUES ('video')");
        jdbcTemplate.update("INSERT INTO Tip_courses (tip_id, course_id) VALUES ('1', '1')");
        jdbcTemplate.update("INSERT INTO Tip_courses (tip_id, course_id) VALUES ('2', '1')");
        jdbcTemplate.update("INSERT INTO Tip_tags (tip_id, tag_id) VALUES ('1', '1')");
        jdbcTemplate.update("INSERT INTO Tip_tags (tip_id, tag_id) VALUES ('2', '1')");
        tipDao.deleteByValue(toMap("tipType", "book"));
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(
                "SELECT c.course_name FROM Tip_courses JOIN courses c on tip_courses.course_id = c.id WHERE tip_id = 1");
        assertThat(maps.size(), is(0));
        maps = jdbcTemplate.queryForList(
                "SELECT t.tag_name FROM Tip_tags JOIN tags t on tip_tags.tag_id = t.id WHERE tip_id = 1");
        assertThat(maps.size(), is(0));
        maps = jdbcTemplate.queryForList(
                "SELECT c.course_name FROM Tip_courses JOIN courses c on tip_courses.course_id = c.id WHERE tip_id = 2");
        assertThat(maps.size(), is(1));
        maps = jdbcTemplate.queryForList(
                "SELECT t.tag_name FROM Tip_tags JOIN tags t on tip_tags.tag_id = t.id WHERE tip_id = 2");
        assertThat(maps.size(), is(1));
    }

    @Test
    public void list() {
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ('course')");
        jdbcTemplate.update("INSERT INTO Tags (tag_name) VALUES ('tag')");
        jdbcTemplate.update("INSERT INTO Tips (tip_type) VALUES ('book')");
        jdbcTemplate.update("INSERT INTO Tips (tip_type) VALUES ('video')");
        jdbcTemplate.update("INSERT INTO Tip_courses (tip_id, course_id) VALUES ('1', '1')");
        jdbcTemplate.update("INSERT INTO Tip_courses (tip_id, course_id) VALUES ('2', '1')");
        jdbcTemplate.update("INSERT INTO Tip_tags (tip_id, tag_id) VALUES ('1', '1')");
        jdbcTemplate.update("INSERT INTO Tip_tags (tip_id, tag_id) VALUES ('2', '1')");
        List<Tip> list = tipDao.list();
        assertThat(list.size(), is(2));
        Tip tip1 = list.get(0);
        assertThat(tip1.getCourses().get(0).getCourseName(), is("course"));
        assertThat(tip1.getTags().get(0).getTagName(), is("tag"));
        Tip tip2 = list.get(1);
        assertThat(tip2.getCourses().get(0).getCourseName(), is("course"));
        assertThat(tip2.getTags().get(0).getTagName(), is("tag"));
    }

}
