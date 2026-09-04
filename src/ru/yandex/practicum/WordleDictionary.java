package ru.yandex.practicum;

import java.util.*;

public class WordleDictionary {
    private final List<String> words;
    private final Set<String> wordSet;

    public WordleDictionary(Collection<String> words) {
        LinkedHashSet<String> normalized = new LinkedHashSet<>();
        for (String word : words) {
            String value = normalize(word);
            if (value.length() == 5 && value.matches("[а-я]+")) {
                normalized.add(value);
            }
        }
        if (normalized.isEmpty()) {
            throw new EmptyDictionaryException("Словарь пятибуквенных слов пуст");
        }
        this.words = List.copyOf(normalized);
        this.wordSet = Set.copyOf(normalized);
    }

    public static String normalize(String word) {
        if (word == null) return "";
        return word.trim().toLowerCase(Locale.ROOT).replace('ё', 'е');
    }

    public boolean contains(String word) {
        return wordSet.contains(normalize(word));
    }

    public List<String> getWords() {
        return words;
    }

    public int size() {
        return words.size();
    }

    public static String check(String answer, String guess) {
        answer = normalize(answer);
        guess = normalize(guess);

        int n = answer.length();
        char[] result = new char[n];
        Arrays.fill(result, '-');
        int[] remaining = new int[Character.MAX_VALUE + 1];

        for (int i = 0; i < n; i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                result[i] = '+';
            } else {
                remaining[answer.charAt(i)]++;
            }
        }

        for (int i = 0; i < n; i++) {
            if (result[i] == '-' && remaining[guess.charAt(i)] > 0) {
                result[i] = '^';
                remaining[guess.charAt(i)]--;
            }
        }
        return new String(result);
    }
}
