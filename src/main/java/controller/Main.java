package controller;

import database.CourseDao;
import database.Dao;
import database.TagDao;
import database.TipDao;
import io.ConsoleIO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import userinterface.UserInterface;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello Lukuvinkkikirjasto!");
        ApplicationContext context = SpringApplication.run(Main.class, args);
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);

        Dao tipDao = new TipDao(jdbcTemplate);
        Dao tagDao = new TagDao(jdbcTemplate);
        Dao courseDao = new CourseDao(jdbcTemplate);
        TipController controller = new TipController(tipDao, courseDao, tagDao);
        UserInterface ui = new UserInterface(controller, new ConsoleIO());
        ui.run();
    }
}
