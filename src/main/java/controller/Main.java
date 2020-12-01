package controller;

import io.ConsoleIO;
import kayttoliittyma.UserInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import tietokanta.CourseDao;
import tietokanta.TagDao;
import tietokanta.TipDao;


@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello Lukuvinkkikirjasto!");
        ApplicationContext context = SpringApplication.run(Main.class, args);
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);

        CourseDao courseDao = new CourseDao(jdbcTemplate);
        TagDao tagDao = new TagDao(jdbcTemplate);
        TipDao tipDao = new TipDao(jdbcTemplate, courseDao, tagDao);
        UserInterface kl = new UserInterface(tipDao, new ConsoleIO());
        kl.run();
    }
}
