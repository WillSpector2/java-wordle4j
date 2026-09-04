package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {
    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(List.of(
                "герой", "гонец", "слово", "домик", "берег"));
        game = new WordleGame(dictionary, new Random(0),
                new PrintWriter(new StringWriter()));
    }

    @Test
    void validMoveConsumesAttempt() throws Exception {
        int before = game.getAttemptsLeft();
        game.makeMove("гонец");
        assertEquals(before - 1, game.getAttemptsLeft());
    }

    @Test
    void invalidWordDoesNotConsumeAttempt() {
        assertThrows(WordNotFoundInDictionary.class,
                () -> game.makeMove("арбуз"));
        assertEquals(6, game.getAttemptsLeft());
    }

    @Test
    void hintBelongsToDictionary() {
        String hint = game.getHint();
        assertTrue(dictionary.contains(hint));
    }

    @Test
    void gameEndsAfterSixValidMoves() throws Exception {
        for (int i = 0; i < 6; i++) {
            if (!game.isFinished()) {
                game.makeMove("гонец");
            }
        }
        assertTrue(game.isFinished());
        assertEquals(0, game.getAttemptsLeft());
    }
}
