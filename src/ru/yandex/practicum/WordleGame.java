package ru.yandex.practicum;

import java.util.*;

public class WordleGame {
    private static final int MAX_ATTEMPTS = 6;

    private final WordleDictionary dictionary;
    private final String answer;
    private final PrintWriterLike log;
    private final Random random;
    private int attemptsLeft = MAX_ATTEMPTS;
    private boolean won;

    private final List<String> guesses = new ArrayList<>();
    private final List<String> hints = new ArrayList<>();

    public WordleGame(WordleDictionary dictionary, Random random, java.io.PrintWriter log) {
        if (dictionary == null || dictionary.size() == 0) {
            throw new IllegalArgumentException("Словарь не может быть пустым");
        }
        this.dictionary = dictionary;
        this.random = Objects.requireNonNull(random, "random");
        this.answer = dictionary.getWords().get(random.nextInt(dictionary.size()));
        this.log = new PrintWriterLike(log);
        this.log.println("Игра начата. Попыток: " + attemptsLeft);
    }

    public String makeMove(String input) throws WordNotFoundInDictionary, InvalidWordException {
        if (isFinished()) {
            throw new IllegalStateException("Игра уже завершена");
        }

        String guess = WordleDictionary.normalize(input);

        if (!guess.matches("[а-я]{5}")) {
            throw new InvalidWordException("Нужно ввести слово из пяти русских букв");
        }
        if (!dictionary.contains(guess)) {
            throw new WordNotFoundInDictionary("Такого слова нет в словаре");
        }

        attemptsLeft--;
        guesses.add(guess);

        String hint = WordleDictionary.check(answer, guess);
        hints.add(hint);

        if (guess.equals(answer)) {
            won = true;
        }

        log.println("Ход: " + guess + " -> " + hint +
                ", осталось попыток: " + attemptsLeft);
        return hint;
    }

    public String getHint() {
        if (isFinished()) return "";
        if (guesses.isEmpty()) {
            return randomCandidate(new HashSet<>());
        }

        Set<String> candidates = new LinkedHashSet<>(dictionary.getWords());

        for (int i = 0; i < guesses.size(); i++) {
            filterCandidates(candidates, guesses.get(i), hints.get(i));
        }

        candidates.removeAll(guesses);

        if (candidates.isEmpty() && !guesses.contains(answer)) {
            candidates.add(answer);
        }

        return randomCandidate(candidates);
    }

    private void filterCandidates(Set<String> candidates, String guess, String hint) {
        candidates.removeIf(candidate -> !matchesFeedback(candidate, guess, hint));
    }

    private boolean matchesFeedback(String candidate, String guess, String expected) {
        return WordleDictionary.check(candidate, guess).equals(expected);
    }

    private String randomCandidate(Set<String> candidates) {
        if (candidates.isEmpty()) return answer;

        List<String> words = new ArrayList<>(candidates);
        return words.get(random.nextInt(words.size()));
    }

    public boolean isFinished() {
        return won || attemptsLeft == 0;
    }

    public boolean isWon() {
        return won;
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getGuesses() {
        return List.copyOf(guesses);
    }

    public List<String> getHints() {
        return List.copyOf(hints);
    }

    private static class PrintWriterLike {
        private final java.io.PrintWriter writer;

        PrintWriterLike(java.io.PrintWriter writer) {
            this.writer = writer;
        }

        void println(String text) {
            if (writer != null) writer.println(text);
        }
    }
}
