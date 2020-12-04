Feature: As a user I want to be able to see all the listed reading tips

  Scenario: start program
    When Program starts
    Then The output should be "Valitse toiminto numerolla"


  Scenario: choose what to save
    When User gives input "1"
    Then The output should be "1. Kirja"