Feature: As a user I want to be able to see all the listed reading tips

  Scenario: start program
    When Program starts
    Then The output should be "Valitse toiminto numerolla"


  Scenario: choose what to save
    When User gives input "1"
    Then The output should be "1. Kirja"

#  Scenario: choose to save a book
#    When User gives input "1\n1\nTestikirja\nKirjailijannimi\2\n1"
#    Then The output should be "Tallennetaanko vinkki näillä tiedoilla?"