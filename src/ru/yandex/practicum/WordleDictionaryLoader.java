package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordleDictionaryLoader {

    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary load(Path path) throws IOException {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(path.toFile(), StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String word = normalize(line);

                if (isValidDictionaryWord(word)) {
                    words.add(word);
                }
            }
        }

        log.println("Загружено слов: " + words.size());

        return new WordleDictionary(words);
    }

    private String normalize(String word) {
        return word
                .trim()
                .toLowerCase(Locale.ROOT)
                .replace('ё', 'е');
    }

    private boolean isValidDictionaryWord(String word) {
        if (word.length() != 5) {
            return false;
        }

        for (char c : word.toCharArray()) {
            if (c < 'а' || c > 'я') {
                return false;
            }
        }

        return true;
    }
}
