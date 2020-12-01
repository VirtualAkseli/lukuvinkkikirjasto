package stepdefs;

import controller.Main;
import io.StubIO;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.util.ArrayList;
import static org.junit.Assert.*;

import java.util.Scanner;
import kayttoliittyma.UserInterface;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import tietokanta.Dao;
import tietokanta.TipDao;

public class Stepdefs {

    UserInterface kayttoliittyma;
    StubIO stubIO;
    TipDao tipDao;
    ArrayList<String> inputLines;
    ApplicationContext context;
    JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        this.inputLines = new ArrayList<>();
        this.context = SpringApplication.run(Main.class);
        this.jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        this.tipDao = new TipDao(jdbcTemplate);
    }

    @Given("Program starts")
    public void program_starts() {
        // for now, we need to tell the UI to quit
        // otherwise, it will keep printing "anna komento"
        inputLines.add("x");
        
        this.stubIO = new StubIO(inputLines);
        this.kayttoliittyma = new UserInterface(tipDao, stubIO);
        kayttoliittyma.run();
    }

    @Then("The output should be {string}")
    public void the_output_should_be(String expected) {
        String printed = "";
        for (String line: stubIO.getPrints()) {
            System.out.println(line);
            printed += line;
        }
        assertTrue(printed.contains(expected));
    }

}
