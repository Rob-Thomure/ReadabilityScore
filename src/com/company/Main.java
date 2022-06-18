package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String text = getTextFromFile(args[0]);
        List<String> sentences = splitIntoSentences(text);
        List<String> words = splitIntoWords(text);
        int characterCount = getCharacterCount(words);
        BigDecimal score = calculateAutomatedReadabilityIndex(sentences.size(), words.size(), characterCount);
        String readingLevel = getReadingLevel(score.setScale(0, RoundingMode.UP).toBigInteger().intValue());
        System.out.println("The text is:");
        System.out.println(text + "\n");
        System.out.printf("Words: %d%n", words.size());
        System.out.printf("Sentences: %d%n", sentences.size());
        System.out.printf("Characters: %d%n", characterCount);
        System.out.printf("The score is: %s%n", score);
        System.out.printf("This text should be understood by %s-year-olds.%n", readingLevel);
    }

    public static String getReadingLevel(int score) {
        switch (score) {
            case 1:
                return "5-6";
            case 2:
                return "6-7";
            case 3:
                return "7-9";
            case 4:
                return "9-10";
            case 5:
                return "10-11";
            case 6:
                return "11-12";
            case 7:
                return "12-13";
            case 8:
                return "13-14";
            case 9:
                return "14-15";
            case 10:
                return "15-16";
            case 11:
                return "16-17";
            case 12:
                return "17-18";
            case 13:
                return "18-24";
            case 14:
                return "24+";
            default:
                return "invalid score";
        }
    }

    public static String getTextFromFile(String file) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(file))) {
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.printf("file not found: %s", file);
        }
        return stringBuilder.toString();
    }

    public static BigDecimal calculateAutomatedReadabilityIndex(int sentenceCount, int wordCount, int characterCount) {
        BigDecimal sentences = BigDecimal.valueOf(sentenceCount);
        BigDecimal words = BigDecimal.valueOf(wordCount);
        BigDecimal characters = BigDecimal.valueOf(characterCount);
        BigDecimal part1 = new BigDecimal("4.71").multiply(characters).divide(words,2, RoundingMode.FLOOR);
        BigDecimal part2 = new BigDecimal("0.5").multiply(words).divide(sentences, 2, RoundingMode.FLOOR);
        BigDecimal score = part1.add(part2).subtract(new BigDecimal("21.43"));
        return score.setScale(2, RoundingMode.FLOOR);
    }

    public static int getCharacterCount(List<String> words) {
        int count = 0;
        for (String word : words) {
            count += word.length();
        }
        return count;
    }

    public static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static double getAverageWordCountPerSentence(List<String> sentences) {
        double sentenceCount = sentences.size();
        double wordCount = 0;
        for (String sentence : sentences) {
            wordCount += sentence.split("\\s").length;
        }
        return wordCount / sentenceCount;
    }

    public static List<String> splitIntoWords(String line) {
        return new ArrayList<>(List.of(line.split("\\s")));
    }

    public static List<String> splitIntoSentences(String line) {
        return new ArrayList<>(List.of(line.split("[.!?]")));
    }

    //** Stage 2: Words and sentences
    public static void stage2Implementation() {
        String line = getInput();
        List<String> sentences = splitIntoSentences(line);
        System.out.println(getAverageWordCountPerSentence(sentences) > 10.0 ? "HARD" : "EASY");
    }

    //** Stage 1: Simplest estimation
    public static void stage1Implementation() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.length() <= 100) {
            System.out.println("EASY");
        } else {
            System.out.println("HARD");
        }
    }
}
