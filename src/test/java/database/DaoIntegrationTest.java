package database;

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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static utilities.MappingUtils.toMap;

public class DaoIntegrationTest {
    private EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("test_schema.sql").build();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(db);
    private TipDao tipDao = new TipDao(jdbcTemplate);
    private CourseDao courseDao = new CourseDao(jdbcTemplate);
    private TagDao tagDao = new TagDao(jdbcTemplate);

    @Test
    public void daoIntegrationTest() {
        Tip tip1 = tipDao.get(tipDao.create(new Tip("book")));
        assertThat(tip1.getTipType(), is("book"));
        Course course1 = courseDao.get(courseDao.create(new Course("Course1")));
        Course course2 = courseDao.get(courseDao.create(new Course("Course2")));
        tip1.setCourses(Arrays.asList(course1, course2));
        tipDao.update(tip1);
        tip1 = tipDao.get(tip1.getId());
        assertThat(tip1.getCourses().size(), is(2));
        assertThat(tip1.getCourses().get(0).getCourseName(), is("Course1"));
        assertThat(tip1.getCourses().get(1).getCourseName(), is("Course2"));
        Tip tip2 = tipDao.get(tipDao.create(new Tip("video")));
        assertThat(tip2.getTipType(), is("video"));
        Tag tag1 = tagDao.get(tagDao.create(new Tag("Tag1")));
        Tag tag2 = tagDao.get(tagDao.create(new Tag("Tag2")));
        tip2.setTags(Arrays.asList(tag1, tag2));
        tipDao.update(tip2);
        tip2 = tipDao.get(tip2.getId());
        assertThat(tip2.getTags().size(), is(2));
        assertThat(tip2.getTags().get(0).getTagName(), is("Tag1"));
        assertThat(tip2.getTags().get(1).getTagName(), is("Tag2"));
        List<Tip> tips = tipDao.list();
        assertThat(tips.get(0).getTipType(), is("book"));
        assertThat(tips.get(1).getTipType(), is("video"));
        tipDao.get(tipDao.create(new Tip("book")));
        List<Tip> byValue = tipDao.getByValue(toMap("tipType", "book"));
        assertThat(byValue.size(), is(2));
        assertThat(byValue.get(1).getId(), is(3L));
        tipDao.deleteByValue(toMap("tipType", "video"));
        try {
            tipDao.get(2L);
            fail();
        } catch (IndexOutOfBoundsException ignored) { }
        tip1.getCourses().remove(0);
        tipDao.update(tip1);
        tip1 = tipDao.get(tip1.getId());
        assertThat(tip1.getCourses().size(), is(1));
        assertThat(tip1.getCourses().get(0).getCourseName(), is("Course2"));
        tipDao.delete(3L);
        assertThat(tipDao.list().size(), is(1));
        assertThat(tipDao.list().get(0).getId(), is(1L));
    }

}
