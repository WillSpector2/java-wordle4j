package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {
    @Test
    void loadsUtf8Dictionary() throws Exception {
        Path file = Files.createTempFile("words_ru", ".txt");
        Files.writeString(
                file,
                "клёст\nгерой\nдом\n",
                java.nio.charset.StandardCharsets.UTF_8
        );

        WordleDictionary dictionary =
                new WordleDictionaryLoader(new PrintWriter(new StringWriter()))
                        .load(file);

        assertTrue(dictionary.contains("клест"));
        assertTrue(dictionary.contains("герой"));
        assertEquals(2, dictionary.size());

        Files.deleteIfExists(file);
    }
}
