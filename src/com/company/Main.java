package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File file = new File(args[0]);
        String text = "";

        try (Scanner scanner = new Scanner(file)) {
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }
            text = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }

        System.out.printf("The text is:%n%s%n%n", text);

        List<String> sentences = splitIntoSentences(text);
        List<String> words = splitIntoWords(sentences);
        int characterCount = getCharacterCount(text);
        List<String> vowelConsonantWords = markVowelsConsonants(words);
        int syllableCount = getSyllableCount(vowelConsonantWords);
        int polysyllableCount = getPolysyllableCount(vowelConsonantWords);

        System.out.printf("Words: %d%n", words.size());
        System.out.printf("Sentences: %d%n", sentences.size());
        System.out.printf("Characters: %d%n", characterCount);
        System.out.printf("Syllables: %d%n", syllableCount);
        System.out.printf("Polysyllables: %d%n", polysyllableCount);


        System.out.print("Enter the score you want to calculate(ARI, FK, SMOG, CL, all:): ");
        Scanner scanner = new Scanner(System.in);
        String scoreType = scanner.nextLine();
        System.out.println();

        BigDecimal automatedReadabilityIndex = calculateAutomatedReadabilityIndex(sentences.size(), words.size(),
                characterCount);
        BigDecimal fleschKincaidTests = calculateFleschKincaidReadability(sentences.size(), words.size(),
                syllableCount);
        BigDecimal SMOGIndex = calculateSMOGIndex(sentences.size(), polysyllableCount);
        BigDecimal colemanLiauIndex = calculateColemanLiauIndex(words, characterCount, sentences);

        String ARIScore = String.format("Automated Readability Index: %s (about %s-year-olds)%n",
                automatedReadabilityIndex, getReadingLevel(automatedReadabilityIndex
                        .setScale(0, RoundingMode.FLOOR).toBigInteger().intValue()));
        String FKScore = String.format("Flesch-kincaid readability tests: %s (about %s-year-olds).%n",
                fleschKincaidTests, getReadingLevel(fleschKincaidTests
                        .setScale(0, RoundingMode.FLOOR).toBigInteger().intValue()));
        String SMOGScore = String.format("Simple Measure of Gobbledygook: %s (about %s-year-olds).%n",
                SMOGIndex, getReadingLevel(SMOGIndex
                        .setScale(0, RoundingMode.FLOOR).toBigInteger().intValue()));
        String CLScore = String.format("Coleman-Liau index: %s (about %s-year-olds).%n",
                colemanLiauIndex, getReadingLevel(colemanLiauIndex
                        .setScale(0, RoundingMode.CEILING).toBigInteger().intValue()));

        switch (scoreType) {
            case "ARI":
                System.out.print(ARIScore);
                break;
            case "FK":
                System.out.print(FKScore);
                break;
            case "SMOG":
                System.out.print(SMOGScore);
                break;
            case "CL":
                System.out.print(CLScore);
                break;
            case "all":
                System.out.print(ARIScore);
                System.out.print(FKScore);
                System.out.print(SMOGScore);
                System.out.print(CLScore);
                break;
            default:
                System.out.println("invalid score");
                break;
        }
    }

    public static int getSyllableCount(List<String> vowelConsonantWords) {
        int count = 0;
        for (String word : vowelConsonantWords) {
            count += word.length();
            if (word.length() == 0) {
                count++;
            }
        }
        return count;
    }

    public static int getPolysyllableCount(List<String> vowelConsonantWords) {
        int count = 0;
        for (String word : vowelConsonantWords) {
            if (word.length() > 2) {
                count++;
            }
        }
        return count;
    }

    public static int getCharacterCount(String text) {
        String characters = text.replaceAll("\\s", "");
        return characters.length();
    }

    public static List<String> markVowelsConsonants(List<String> words) {
        List<String> vowelConsonantWords = new ArrayList<>();
        for (String word : words) {
            String vowelsConsonants = word.replaceAll("[vV]", "c");
            vowelsConsonants = vowelsConsonants.replaceAll("[aeiouyAEIOUY]{2}", "v");
            vowelsConsonants = vowelsConsonants.replaceAll("e\\b", "c");
            vowelsConsonants = vowelsConsonants.replaceAll("[aeiouAEIOU]", "v");
            vowelsConsonants = vowelsConsonants.replaceAll("[^v]", "");
            vowelConsonantWords.add(vowelsConsonants);
        }
        return vowelConsonantWords;
    }

    public static String getReadingLevel(int score) {
        switch (score) {
            case 1:
                return "6";
            case 2:
                return "6-7";
            case 3:
                return "9";
            case 4:
                return "10";
            case 5:
                return "11";
            case 6:
                return "12";
            case 7:
                return "13";
            case 8:
                return "14";
            case 9:
                return "15";
            case 10:
                return "16";
            case 11:
                return "17";
            case 12:
                return "18";
            case 13:
                return "24";
            case 14:
                return "24+";
            default:
                return "invalid score";
        }
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

    public static BigDecimal calculateFleschKincaidReadability(int sentenceCount, int wordCount, int syllableCount) {
        BigDecimal sentences = BigDecimal.valueOf(sentenceCount);
        BigDecimal words = BigDecimal.valueOf(wordCount);
        BigDecimal syllables = BigDecimal.valueOf(syllableCount);
        BigDecimal part1 = new BigDecimal("0.39").multiply(words).divide(sentences, 2, RoundingMode.FLOOR);
        BigDecimal part2 = new BigDecimal("11.8").multiply(syllables).divide(words, 2, RoundingMode.FLOOR);
        BigDecimal score = part1.add(part2).subtract(new BigDecimal("15.59"));
        return score.setScale(2, RoundingMode.FLOOR);
    }

    public static BigDecimal calculateSMOGIndex(int sentenceCount, int polysyllableCount) {
        BigDecimal sentences = BigDecimal.valueOf(sentenceCount);
        BigDecimal polysyllables = BigDecimal.valueOf(polysyllableCount);
        BigDecimal score = polysyllables.multiply(new BigDecimal("30"))
                .divide(sentences, 2, RoundingMode.FLOOR).sqrt(new MathContext(2))
                .multiply(new BigDecimal("1.043")).add(new BigDecimal("3.1291"));
        return score.setScale(2, RoundingMode.FLOOR);
    }

    public static BigDecimal calculateColemanLiauIndex(List<String> words, int characterCount, List<String> sentences) {
        BigDecimal numWords = BigDecimal.valueOf(words.size());
        BigDecimal numCharacters = BigDecimal.valueOf(characterCount);
        BigDecimal averageNumCharactersPer100Words = numCharacters.divide(numWords, 4, RoundingMode.FLOOR)
                .multiply(new BigDecimal("100"));
        BigDecimal numSentences = BigDecimal.valueOf(sentences.size());
        BigDecimal averageNumberSentencesPer100Words = numSentences.divide(numWords, 4, RoundingMode.FLOOR)
                .multiply(new BigDecimal("100"));
        BigDecimal part1 = new BigDecimal("0.0588").multiply(averageNumCharactersPer100Words);
        BigDecimal part2 = new BigDecimal("0.296").multiply(averageNumberSentencesPer100Words);
        BigDecimal score = part1.subtract(part2).subtract(new BigDecimal("15.8"));
        return score.setScale(2, RoundingMode.FLOOR);
    }

    public static List<String> splitIntoWords(List<String> sentences) {
        List<String> words = new ArrayList<>();
        for (String sentence : sentences) {
            String newSentence = sentence.replaceAll("[,\"]", "");
            words.addAll(List.of(newSentence.split("\\s")));
        }
        return words;
    }

    public static List<String> splitIntoSentences(String line) {
        List<String> sentences = new ArrayList<>(List.of(line.split("[.!?]")));
        for (int i = 0; i < sentences.size(); i++) {
            sentences.set(i, sentences.get(i).trim());
        }
        return sentences;
    }

}
