package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordleGame {

    private static final int MAX_ATTEMPTS = 6;

    private final WordleDictionary dictionary;
    private final String answer;
    private final Random random;
    private final PrintWriter log;

    private int attemptsLeft;
    private boolean won;

    private final List<String> guesses;
    private final List<String> hints;
    private final Set<String> shownHints;

    public WordleGame(
            WordleDictionary dictionary,
            String answer,
            Random random,
            PrintWriter log) {

        if (dictionary == null || dictionary.size() == 0) {
            throw new IllegalArgumentException("Словарь не может быть пустым");
        }

        if (answer == null || !dictionary.contains(answer)) {
            throw new IllegalArgumentException(
                    "Загаданное слово должно находиться в словаре");
        }

        if (random == null) {
            throw new IllegalArgumentException("Random не может быть null");
        }

        if (log == null) {
            throw new IllegalArgumentException("Лог не может быть null");
        }

        this.dictionary = dictionary;
        this.answer = answer;
        this.random = random;
        this.log = log;

        this.attemptsLeft = MAX_ATTEMPTS;
        this.won = false;

        this.guesses = new ArrayList<>();
        this.hints = new ArrayList<>();
        this.shownHints = new HashSet<>();

        log.println("Игра начата");
        log.println("Количество попыток: " + attemptsLeft);
    }

    public String makeMove(String input) {

        if (isFinished()) {
            throw new IllegalStateException("Игра уже завершена");
        }

        String guess = input;

        if (guess == null || !guess.matches("[а-я]{5}")) {
            throw new InvalidWordException(
                    "Введите слово из пяти русских букв");
        }

        if (!dictionary.contains(guess)) {
            throw new WordNotFoundInDictionary(
                    "Такого слова нет в словаре");
        }

        attemptsLeft--;

        guesses.add(guess);

        String hint = WordleDictionary.check(answer, guess);

        hints.add(hint);

        if (guess.equals(answer)) {
            won = true;
        }

        log.println(
                "Слово: " + guess
                        + ", результат: " + hint
                        + ", осталось попыток: " + attemptsLeft
        );

        return hint;
    }

    public String getHint() {

        if (isFinished()) {
            return "";
        }

        Set<String> candidates = new HashSet<>(dictionary.getWords());

        for (int i = 0; i < guesses.size(); i++) {
            String guess = guesses.get(i);
            String hint = hints.get(i);

            candidates.removeIf(
                    candidate ->
                            !WordleDictionary.check(candidate, guess)
                                    .equals(hint)
            );
        }

        candidates.removeAll(guesses);

        candidates.removeAll(shownHints);

        if (candidates.isEmpty()) {
            throw new IllegalStateException(
                    "Не осталось новых слов для подсказки"
            );
        }

        List<String> candidateList = new ArrayList<>(candidates);

        String hint = candidateList.get(
                random.nextInt(candidateList.size())
        );

        shownHints.add(hint);

        log.println("Подсказка: " + hint);

        return hint;
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
        return new ArrayList<>(guesses);
    }

    public List<String> getHints() {
        return new ArrayList<>(hints);
    }
}
