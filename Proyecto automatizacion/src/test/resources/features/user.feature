Feature: My automation with datadriven

  Scenario Outline: Successfully check user
    Given the user <user> and password <pass>
    Examples:
      | user | pass |
      | a    | 1    |
      #| @externaldata@/data.csv |