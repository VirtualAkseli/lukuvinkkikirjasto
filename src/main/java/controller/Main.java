package controller;

import database.Dao;
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
        UserInterface kl = new UserInterface(tipDao, new ConsoleIO());
        kl.run();
    }
}
