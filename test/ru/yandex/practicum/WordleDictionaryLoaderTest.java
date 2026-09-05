package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordleDictionaryLoaderTest {

    @Test
    void loadsAndNormalizesUtf8Dictionary() throws Exception {
        Path file = Files.createTempFile("words_ru", ".txt");

        Files.writeString(
                file,
                "клёст\nгерой\nдом\n ТРЁПА \n",
                StandardCharsets.UTF_8
        );

        WordleDictionary dictionary =
                new WordleDictionaryLoader(
                        new PrintWriter(new StringWriter())
                ).load(file);

        assertTrue(dictionary.contains("клест"));
        assertTrue(dictionary.contains("герой"));
        assertTrue(dictionary.contains("трепа"));

        assertEquals(3, dictionary.size());

        Files.deleteIfExists(file);
    }
}