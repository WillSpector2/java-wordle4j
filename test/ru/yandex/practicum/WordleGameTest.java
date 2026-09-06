package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordleGameTest {

    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(
                List.of(
                        "герой",
                        "гонец",
                        "слово",
                        "домик",
                        "берег"
                )
        );

        game = new WordleGame(
                dictionary,
                "герой",
                new Random(0),
                new PrintWriter(new StringWriter())
        );
    }

    @Test
    void validMoveConsumesAttempt() {
        int before = game.getAttemptsLeft();

        game.makeMove("гонец");

        assertEquals(
                before - 1,
                game.getAttemptsLeft()
        );
    }

    @Test
    void invalidWordDoesNotConsumeAttempt() {
        assertThrows(
                WordNotFoundInDictionary.class,
                () -> game.makeMove("арбуз")
        );

        assertEquals(6, game.getAttemptsLeft());
    }

    @Test
    void hintBelongsToDictionary() {
        String hint = game.getHint();

        assertTrue(dictionary.contains(hint));
    }

    @Test
    void hintDoesNotRepeatPreviousGuess() {
        String firstHint = game.getHint();

        game.makeMove("гонец");

        String secondHint = game.getHint();

        assertNotEquals(firstHint, secondHint);
        assertNotEquals("гонец", secondHint);
    }

    @Test
    void hintIsNotThePreviousGuess() {
        game.makeMove("гонец");

        String hint = game.getHint();

        assertNotEquals("гонец", hint);
    }

    @Test
    void correctAnswerFinishesGame() {
        game.makeMove("герой");

        assertTrue(game.isFinished());
        assertTrue(game.isWon());
    }

    @Test
    void hintsAreNotRepeated() {
        String firstHint = game.getHint();
        String secondHint = game.getHint();

        assertNotEquals(firstHint, secondHint);
    }

    @Test
    void gameEndsAfterSixValidMoves() {
        for (int i = 0; i < 6; i++) {
            if (!game.isFinished()) {
                game.makeMove("гонец");
            }
        }

        assertTrue(game.isFinished());
        assertEquals(0, game.getAttemptsLeft());
    }
}
