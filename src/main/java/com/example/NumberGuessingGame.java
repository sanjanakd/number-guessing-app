package com.example;


import org.springframework.beans.factory.annotation.Autowired;

import static com.example.GameConstants.*;


public class NumberGuessingGame {

    //we can autowire it as well.
    @Autowired
    private ConsoleService consoleService = new ConsoleService();
    private double currentNumber = 50;
    private double OFFSET = 100;
    private double greaterThan = 0;
    private double lowerThan = 0;
    private boolean forwardMove = false;
    private boolean backwardMove = false;
    private boolean initialLoop = true;
    private int numberOFGuesses;
    private double newCurrentNumber;

    //just in case if you need to set your own offset and startup number
    public NumberGuessingGame(double initialNumberInput, double currentNumberInput) {
        this.OFFSET = initialNumberInput;
        this.currentNumber = currentNumberInput;
    }


    public NumberGuessingGame() {

    }

    /**
     * game starter
     */
    final public void startGame() {

        // Check if user is ready to play
        consoleService.printItOnConsole(READY_TO_PLAY_PROMPT);

        if (!isUserReadyToPlay()) {
            consoleService.printItOnConsole(EXITING_GOOD_BY_MSG);

        } else {

            while (true) {

                consoleService.printItOnConsole("Computer: is the number " + new Double(currentNumber).intValue() + " ?\n\n");

                String response = consoleService.readUserInput(System.in);

                //Check is user wants to exit the game
                if (EXIT.equalsIgnoreCase(response)) {

                    break;

                } else if (INPUT_FOR_CORRECT_GUESS_PROMPT.equalsIgnoreCase(response)) {

                    consoleService.printItOnConsole("\n\nThanks!!! guessed it in " + numberOFGuesses + " guesses.\n\n");
                    break;

                } else if (INPUT_FOR_HIGHER_PROMPT.equalsIgnoreCase(response) || INPUT_FOR_LOWER_PROMPT.equalsIgnoreCase(response)) {

                    // Validate if user missed his number or  entered higher instead of lower or vic versa.
                    if (validateUserInput()) {
                        break;
                    }
                    newCurrentNumber = calculateNewNumber(response);


                } else {
                    // if user enter invalid input value, show user valid values
                    consoleService.printItOnConsole(INVALID_INPUT_TRY_HIGHER_OR_LOWER_OR_YES);
                }
            }
        }
    }

    private boolean validateUserInput() {
        // Validate if user missed his number or  entered higher instead of lower or vic versa.
        if (greaterThan - lowerThan == 1 || greaterThan - lowerThan == -1) {
            consoleService.printItOnConsole("\n\nAre you sure your number is not " + greaterThan + " or " + lowerThan + " ? if you are not sure, exit the game and try again!\n\n");
            String isNumberBetween = consoleService.readUserInput(System.in);

            if (INPUT_FOR_CORRECT_GUESS_PROMPT.equalsIgnoreCase(isNumberBetween)) {
                consoleService.printItOnConsole("\n\nThanks!!! guessed it in " + numberOFGuesses + " guesses.\n\n");
                return true;
            } else {
                return true;
            }
        }
        return false;
    }

    final public double calculateNewNumber(String response) {

        //These flag helps to track the user movement, backward or forward from initial number
        setInitialFlags(response);
        numberOFGuesses++;

        //I could reduce the use of extensive variable but kept it more verbose for easier understanding
        if (INPUT_FOR_HIGHER_PROMPT.equalsIgnoreCase(response)) {

            greaterThan = currentNumber;

            if (forwardMove) {

                currentNumber = currentNumber + Math.pow(OFFSET, numberOFGuesses);

            } else {
                currentNumber = greaterThan + Math.round((lowerThan - greaterThan) / 2);
                backwardMove = false;
            }

            return currentNumber;

        } else if (INPUT_FOR_LOWER_PROMPT.equalsIgnoreCase(response)) {

            lowerThan = currentNumber;

            if (backwardMove) {

                currentNumber = currentNumber - Math.pow(OFFSET, numberOFGuesses);

            } else {
                currentNumber = greaterThan - Math.round((greaterThan - lowerThan) / 2);
                forwardMove = false;
            }

            return currentNumber;
        } else {

            return 0;
        }
    }


    /**
     * Helper method to set some flags, needed to detect if user is moving forward or backward
     * from initial number
     *
     * @param response
     * @return
     */
    private boolean setInitialFlags(String response) {

        if (initialLoop) {
            if (INPUT_FOR_HIGHER_PROMPT.equalsIgnoreCase(response)) {
                forwardMove = true;
            }
            if (INPUT_FOR_LOWER_PROMPT.equalsIgnoreCase(response)) {
                backwardMove = true;
            }
            initialLoop = false;
        }
        return initialLoop;
    }


    /**
     * Validates if user input to start the game and returns true if user is ready to play
     * the game
     *
     * @return
     */
    final public boolean isUserReadyToPlay() {

        String ready = consoleService.readUserInput(System.in);

        while (!VALID_INPUT_FOR_READY_PROMPT.equalsIgnoreCase(ready) || EXIT.equalsIgnoreCase(ready)) {

            if (EXIT.equalsIgnoreCase(ready)) {
                return false;
            }
            consoleService.printItOnConsole(INVALID_INPUT_MSG_FOR_READY_PROMPT);
            ready = consoleService.readUserInput(System.in);
        }
        return true;
    }


    public int numberOFGuesses() {
        return this.numberOFGuesses;
    }

    public int getNewCurrentNumber() {
        return new Double(newCurrentNumber).intValue();
    }

}