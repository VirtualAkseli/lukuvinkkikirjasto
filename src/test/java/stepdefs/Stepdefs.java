package stepdefs;

import controller.Main;
import controller.TipController;
import database.StubTipDao;
import io.StubIO;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import userinterface.UserInterface;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class Stepdefs {

    UserInterface kayttoliittyma;
    StubIO stubIO;
    StubTipDao std;
    ArrayList<String> inputLines;
    ApplicationContext context;
    JdbcTemplate jdbcTemplate;
    TipController tipController;

    @Before
    public void setup() {
        this.inputLines = new ArrayList<>();
        //this.context = SpringApplication.run(Main.class);
        //this.jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        this.std = new StubTipDao();
        this.tipController = new TipController(std);
    }

    @When("Program starts")
    public void program_starts() {

        //ApplicationContext context = SpringApplication.run(Main.class);
        //JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        //this.tipDao = new TipDao(jdbcTemplate);
        // for now, we need to tell the UI to quit
        // otherwise, it will keep printing the commands
//        inputLines.add("4");
//
//        this.stubIO = new StubIO(inputLines);
//        this.kayttoliittyma = new UserInterface(tipController, stubIO);
//        kayttoliittyma.run();

    }

    @When("User gives input {string}")
    public void user_gives_input(String command) {
//        inputLines.add(command);
//        
//        // then tell program to back out
//        inputLines.add("5");
//        inputLines.add("4");
//        this.stubIO = new StubIO(inputLines);
//        this.kayttoliittyma = new UserInterface(tipController, stubIO);
//        kayttoliittyma.run();
    }

    @Then("The output should be {string}")
    public void the_output_should_be(String expected) {
//        String printed = "";
//        for (String line : stubIO.getPrints()) {
//            // System.out.println("line: " + line);
//            printed += line;
//        }
//        assertTrue(printed.contains(expected));
    }

}
