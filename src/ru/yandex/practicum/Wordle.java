package ru.yandex.practicum;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {

        try (PrintWriter log = new PrintWriter(
                new BufferedWriter(
                        new FileWriter("wordle.log", true)
                ))) {

            Path dictionaryPath = args.length > 0
                    ? Paths.get(args[0])
                    : Paths.get("words_ru.txt");

            WordleDictionaryLoader loader =
                    new WordleDictionaryLoader(log);

            WordleDictionary dictionary =
                    loader.load(dictionaryPath);

            List<String> words = dictionary.getWords();

            Random random = new Random();

            String answer =
                    words.get(random.nextInt(words.size()));

            WordleGame game =
                    new WordleGame(
                            dictionary,
                            answer,
                            random,
                            log
                    );

            try (Scanner scanner = new Scanner(System.in)) {

                System.out.println(
                        "Игра Wordle. Угадайте слово из 5 букв."
                );

                System.out.println(
                        "Нажмите Enter без слова, чтобы получить подсказку."
                );

                while (!game.isFinished()) {

                    System.out.print("> ");

                    String input = scanner.nextLine().trim();

                    if (input.isEmpty()) {
                        String hint = game.getHint();

                        System.out.println(
                                "Подсказка: " + hint
                        );

                        continue;
                    }

                    input = input
                            .toLowerCase(Locale.ROOT)
                            .replace('ё', 'е');

                    try {
                        String result = game.makeMove(input);

                        System.out.println(input);
                        System.out.println(result);

                    } catch (InvalidWordException e) {

                        System.out.println(e.getMessage());

                    } catch (WordNotFoundInDictionary e) {

                        System.out.println(e.getMessage());
                    }
                }
            }

            System.out.println(
                    "# загаданное слово: " + game.getAnswer()
            );

            if (game.isWon()) {
                System.out.println("Победа!");
            } else {
                System.out.println("Попытки закончились.");
            }

        } catch (Exception e) {

            try (PrintWriter errorLog = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter("wordle.log", true)
                    ))) {

                e.printStackTrace(errorLog);

            } catch (IOException ignored) {
            }
        }
    }
}
