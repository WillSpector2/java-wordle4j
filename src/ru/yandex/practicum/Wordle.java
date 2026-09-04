package ru.yandex.practicum;

import java.io.*;
import java.nio.file.*;
import java.util.Random;
import java.util.Scanner;

public class Wordle {
    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter(
                new BufferedWriter(new FileWriter("wordle.log", true)))) {

            Path dictionaryPath = args.length > 0
                    ? Paths.get(args[0])
                    : Paths.get("words_ru.txt");

            WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
            WordleDictionary dictionary = loader.load(dictionaryPath);
            WordleGame game = new WordleGame(dictionary, new Random(), log);

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Игра Wordle. Угадайте слово из 5 букв.");
                System.out.println("Enter без слова — получить подсказку.");

                while (!game.isFinished()) {
                    System.out.print("> ");
                    String input = scanner.nextLine();

                    if (input.trim().isEmpty()) {
                        String hint = game.getHint();
                        System.out.println("Подсказка: " + hint);
                        continue;
                    }

                    try {
                        String normalized = WordleDictionary.normalize(input);
                        String result = game.makeMove(normalized);
                        System.out.println("> " + normalized);
                        System.out.println(result);
                    } catch (InvalidWordException | WordNotFoundInDictionary e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

            System.out.println("# загаданное слово: " + game.getAnswer());
            if (game.isWon()) {
                System.out.println("Победа!");
            } else {
                System.out.println("Попытки закончились.");
            }

        } catch (Exception e) {
            try (PrintWriter errorLog = new PrintWriter(
                    new BufferedWriter(new FileWriter("wordle.log", true)))) {
                e.printStackTrace(errorLog);
            } catch (IOException ignored) {
            }
        }
    }
}
