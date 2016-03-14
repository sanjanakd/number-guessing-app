package com.example;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.InputStream;

import static com.example.GameConstants.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class NumberGuessingGameTest extends TestCase {

    private static final String INVALID_INPUT_FOR_READY_PROMPT = "ready1"; //Prompt

    @Mock
    private ConsoleService consoleService;

    @InjectMocks
    private NumberGuessingGame numberGuessingGame = new NumberGuessingGame(50, 50);


    /**
     * Test for positive number.
     */
    @Test
    public void testForCorrectGuessesForNumberPositive() {

        when(consoleService.readUserInput(any(InputStream.class))).
                thenReturn(VALID_INPUT_FOR_READY_PROMPT).
                thenReturn(INPUT_FOR_HIGHER_PROMPT).
                thenReturn(INPUT_FOR_LOWER_PROMPT).
                thenReturn(INPUT_FOR_LOWER_PROMPT).
                thenReturn(INPUT_FOR_LOWER_PROMPT).
                thenReturn(INPUT_FOR_LOWER_PROMPT).
                thenReturn(INPUT_FOR_HIGHER_PROMPT).
                thenReturn(INPUT_FOR_LOWER_PROMPT).
                thenReturn(INPUT_FOR_CORRECT_GUESS_PROMPT); // to exit the test.


        doAnswer(new PrintOnConsole()).when(consoleService).printItOnConsole(anyString());

        numberGuessingGame.startGame();

        assertEquals("unexpected number ", 54, numberGuessingGame.getNewCurrentNumber());
        verify(consoleService).printItOnConsole(READY_TO_PLAY_PROMPT);
        verify(consoleService, times(10)).printItOnConsole(any(String.class));
        verify(consoleService, times(9)).readUserInput(any(InputStream.class));
        assertEquals("took more attempt than needed", 7, numberGuessingGame.numberOFGuesses());
    }


    /**
     * Test for negative number
     */
    @Test
    public void testForCorrectGuessesForNumberForNegative() {

        when(consoleService.readUserInput(any(InputStream.class))).
                thenReturn(VALID_INPUT_FOR_READY_PROMPT).
                thenReturn(INPUT_FOR_LOWER_PROMPT).
                thenReturn(INPUT_FOR_LOWER_PROMPT).
                thenReturn(INPUT_FOR_HIGHER_PROMPT).
                thenReturn(INPUT_FOR_HIGHER_PROMPT).
                thenReturn(INPUT_FOR_HIGHER_PROMPT).
                thenReturn(INPUT_FOR_HIGHER_PROMPT).
                thenReturn(INPUT_FOR_CORRECT_GUESS_PROMPT); // to exit the test.

        doAnswer(new PrintOnConsole()).when(consoleService).printItOnConsole(anyString());

        numberGuessingGame.startGame();

        verify(consoleService).printItOnConsole(READY_TO_PLAY_PROMPT);
        verify(consoleService, times(9)).printItOnConsole(any(String.class));
        verify(consoleService, times(8)).readUserInput(any(InputStream.class));
        assertEquals("Took unexpected number of attempts to guess the number", 6, numberGuessingGame.numberOFGuesses());
        assertEquals("unexpected number ", -156, numberGuessingGame.getNewCurrentNumber());
    }


    /**
     * Test for if user enters wrong input for ready prompt.
     */
    @Test
    public void testUserInputForReadyIsIncorrect() {

        //When:  user entered wrong input for ready prompt and then exit.
        when(consoleService.readUserInput(any(InputStream.class))).thenReturn(INVALID_INPUT_FOR_READY_PROMPT).
                thenReturn(INVALID_INPUT_FOR_READY_PROMPT).
                thenReturn(EXIT); // to exit the test.

        doAnswer(new PrintOnConsole()).when(consoleService).printItOnConsole(anyString());

        numberGuessingGame.startGame();

        verify(consoleService).printItOnConsole(READY_TO_PLAY_PROMPT);
        verify(consoleService, times(2)).printItOnConsole(INVALID_INPUT_MSG_FOR_READY_PROMPT);
        verify(consoleService, times(3)).readUserInput(any(InputStream.class));
    }


    /**
     * Test for {@link NumberGuessingGame#isUserReadyToPlay()}
     */
    @Test
    public void testIsUserReadyToPlay() {

        when(consoleService.readUserInput(any(InputStream.class))).thenReturn(VALID_INPUT_FOR_READY_PROMPT);

        boolean isUserReady = numberGuessingGame.isUserReadyToPlay();

        verify(consoleService, times(1)).readUserInput(any(InputStream.class));
        assertTrue("unexpected value", isUserReady);
    }


    /**
     * Test if user enters invalid input for "Ready to play" prompt?
     */
    @Test
    public void testInvalidInputToReadyPrompt() {

        when(consoleService.readUserInput(any(InputStream.class)))
                .thenReturn(INVALID_INPUT_FOR_READY_PROMPT)
                .thenReturn(INVALID_INPUT_FOR_READY_PROMPT)
                .thenReturn(EXIT); // to exit the program

        boolean isUserReady = numberGuessingGame.isUserReadyToPlay();

        verify(consoleService, times(3)).readUserInput(any(InputStream.class));
        assertFalse("unexpected value", isUserReady);
    }


    @Test
    public void testCalculateNewNumberForInputHigher() {
        double newNumber = numberGuessingGame.calculateNewNumber(INPUT_FOR_HIGHER_PROMPT);
        assertEquals("unexpected input ", 100.0, newNumber);
    }


    @Test
    public void testCalculateNewNumberForInputLower() {
        double newNumber = numberGuessingGame.calculateNewNumber(INPUT_FOR_LOWER_PROMPT);
        assertEquals("unexpected input ", 0.0, newNumber);
    }


    /**
     * Test if user enters invalid input for "Ready to play" prompt?
     */
    @Test
    public void testExitInputToReadyPrompt() {

        when(consoleService.readUserInput(any(InputStream.class)))
                .thenReturn(EXIT); // to exit the program

        boolean isUserReady = numberGuessingGame.isUserReadyToPlay();

        verify(consoleService, times(1)).readUserInput(any(InputStream.class));
        assertFalse("unexpected value", isUserReady);
    }


    /**
     * Answer for printConsole method
     */
    private class PrintOnConsole implements Answer {

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            String arg = (String) invocation.getArguments()[0];
            System.out.println(arg);
            return arg;
        }
    }

}
