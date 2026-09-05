package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordleDictionaryTest {

    @Test
    void containsWordsFromDictionary() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("слово", "герой")
        );

        assertTrue(dictionary.contains("слово"));
        assertTrue(dictionary.contains("герой"));
        assertFalse(dictionary.contains("дом"));
        assertEquals(2, dictionary.size());
    }

    @Test
    void returnsCopyOfWords() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("слово", "герой")
        );

        List<String> words = dictionary.getWords();
        words.clear();

        assertEquals(2, dictionary.size());
    }

    @Test
    void checksRepeatedLettersCorrectly() {
        assertEquals(
                "+^-^-",
                WordleDictionary.check("герой", "гонец")
        );
    }
}