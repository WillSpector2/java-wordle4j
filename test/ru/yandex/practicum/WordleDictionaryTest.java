package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {
    @Test
    void normalizesWords() {
        assertEquals("елка", WordleDictionary.normalize(" ЁЛКА "));
    }

    @Test
    void keepsOnlyFiveRussianLetterWords() {
        WordleDictionary d = new WordleDictionary(
                List.of("слово", "дом", "ABC", "елка!", "герой"));
        assertTrue(d.contains("СЛОВО"));
        assertTrue(d.contains("герой"));
        assertEquals(2, d.size());
    }

    @Test
    void checksRepeatedLettersCorrectly() {
        assertEquals("+^-^-", WordleDictionary.check("герой", "гонец"));
    }
}
