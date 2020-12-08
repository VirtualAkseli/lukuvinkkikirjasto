package database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import tiplogic.Course;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static utilities.MappingUtils.toMap;

public class CourseDaoTest {
    private EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("courses.sql").build();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
    private CourseDao courseDao = new CourseDao(jdbcTemplate);

    @Before
    public void before() {
        jdbcTemplate.execute("TRUNCATE TABLE Courses; ALTER TABLE Courses ALTER COLUMN id RESTART WITH 1;");
    }

    @After
    public void after() {
        db.shutdown();
    }

    @Test
    public void courseNameIsCorrectWhenRetrievedById() {
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ('name')");
        Course out = courseDao.get(1L);
        assertThat(out.getCourseName(), is("name"));
    }

    @Test
    public void courseNameIsCorrectWhenRetrievedByName() {
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ('name')");
        List<Course> list = courseDao.getByValue(toMap("courseName", "name"));
        assertThat(list.size(), is(1));
        Course out = list.get(0);
        assertThat(out.getCourseName(), is("name"));
    }

    @Test
    public void courseNameIsCorrectAfterUpdate() {
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ( ? )", "old name");
        Course in = new Course();
        in.setId(1L);
        in.setCourseName("name");
        courseDao.update(in);
        Course out = jdbcTemplate.query("SELECT * FROM Courses WHERE id = 1",
                new BeanPropertyRowMapper<>(Course.class)).get(0);
        assertThat(out.getCourseName(), is("name"));
    }

    @Test
    public void deletingCourseReflectedCorrectlyInNumberOfCourses() {
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ( ? )", "old name");;
        courseDao.delete(1L);
        List<Course> out = jdbcTemplate.query("SELECT * FROM Courses WHERE id = 1",
                new BeanPropertyRowMapper<>(Course.class));
        assertThat(out.size(), is(0));
    }

    @Test
    public void courseCanBeDeletedByItsNAme() {
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ('name')");
        courseDao.deleteByValue(toMap("courseName", "name"));
        List<Course> out = jdbcTemplate.query("SELECT * FROM Courses WHERE id = 1",
                new BeanPropertyRowMapper<>(Course.class));
        assertThat(out.size(), is(0));
    }

    
    @Test
    public void list() {
        jdbcTemplate.update("INSERT INTO Courses (course_name) VALUES ('1'), ('2'), ('3')");
        List<Course> list = courseDao.list();
        Function<List<Course>, Integer> getSum = (courses) ->
                courses.stream().map(Course::getCourseName).mapToInt(s ->
                        s != null && (s).matches("\\d+") ? Integer.parseInt(s) : 0).sum();

        assertThat(list.get(0).getId(), is(1L));
        assertThat(list.get(1).getId(), is(2L));
        assertThat(list.get(2).getId(), is(3L));
        assertThat(getSum.apply(list), is(IntStream.range(1, 4).sum()));
    }

}
