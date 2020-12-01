package stepdefs;

import controller.Main;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.io.PrintStream;
import java.util.Scanner;
import kayttoliittyma.Kayttoliittyma;
import static org.junit.Assert.*;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import tietokanta.Dao;
import tietokanta.TipDao;

public class Stepdefs {

    Kayttoliittyma kayttoliittyma;
    Dao bookDao;
    Scanner lukija;

    @Given("Program starts")
    public void program_starts() {
        ApplicationContext context = SpringApplication.run(Main.class);
        JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        this.bookDao = new TipDao(jdbcTemplate);
        this.lukija = new Scanner(System.in);
    }

    @Then("The output should be {string}")
    public void the_output_should_be(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}
