package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordleDictionary {

    private final List<String> words;
    private final Set<String> wordSet;

    public WordleDictionary(Collection<String> words) {
        if (words == null || words.isEmpty()) {
            throw new EmptyDictionaryException("Словарь пуст");
        }

        this.words = new ArrayList<>(words);

        if (this.words.isEmpty()) {
            throw new EmptyDictionaryException("Словарь пуст");
        }

        this.wordSet = new HashSet<>(this.words);
    }

    public boolean contains(String word) {
        return wordSet.contains(word);
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    public int size() {
        return words.size();
    }

    public static String check(String answer, String guess) {
        char[] result = new char[5];
        boolean[] used = new boolean[5];

        for (int i = 0; i < 5; i++) {
            result[i] = '-';
        }

        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                result[i] = '+';
                used[i] = true;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (result[i] == '+') {
                continue;
            }

            for (int j = 0; j < 5; j++) {
                if (!used[j] && guess.charAt(i) == answer.charAt(j)) {
                    result[i] = '^';
                    used[j] = true;
                    break;
                }
            }
        }

        return new String(result);
    }
}