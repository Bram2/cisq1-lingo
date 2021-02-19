  Feature: Training for lingo
    As a lingo participant
    I want to practice guessing words
    In order to prepare for my TV appearance

  Scenario: Starting a game
    When I start a new game
    Then the word to guess hass "5" letters
    And I should see the first letter
    And my score is "0"

    Scenario Outline: Start a new round
      Given I am playing a game
      And the round was won
      And the last word had "<previous length>" letters
      When I start a new round
      Then the the word to guess has "<next length>" letters

      Examples:
        | previous length | next length |
        | 5               | 6           |
        | 6               | 7           |
        | 7               | 5           |


  Scenario Outline: Guessing a word
    Given I am playing a game
    And the word to guess is "<word>"
    When I try to "<Guess>" the word
    Then I expect to receive "<feedback>" with a "<shown hint>"

    Examples:
    | word | shown hint               | Guess       | feedback                                             |
    | BAARD| 'B', '.', '.', '.', '.'  | BERGEN      | INVALID, INVALID, INVALID, INVALID, INVALID, INVALID |
    | BAARD| 'B', '.', '.', '.', '.'  | BONJE       | CORRECT, ABSENT, ABSENT, ABSENT ,ABSENT              |
    | BAARD| 'B', '.', '.', '.', '.'  | BARST       | CORRECT, CORRECT, PRESENT, ABSENT, ABSENT            |
    | BAARD| 'B', 'A', '.', '.', '.'  | DRAAD       | ABSENT, PRESENT, CORRECT, PRESENT, CORRECT           |
    | BAARD| 'B', 'A', 'A', '.', 'D'  |BAARD        | CORRECT, CORRECT, CORRECT, CORRECT, CORRECT          |