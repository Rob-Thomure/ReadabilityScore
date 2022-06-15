package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String line = getInput();
        List<String> sentences = splitIntoSentences(line);
        System.out.println(getAverageWordCountPerSentence(sentences) > 10.0 ? "HARD" : "EASY");
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

    public static List<String> splitIntoSentences(String line) {
        List<String> sentences = new ArrayList<>(List.of(line.split("[.!?]")));
        List<String> sentencesWithPunctuation = new ArrayList<>();
        int listSize = sentences.size();
        int characterCount = 0;
        for (int i = 0; i < listSize; i++) {
            int sentenceSize = sentences.get(i).length();
            if (characterCount + sentenceSize < line.length()) {
                char punctuation = line.charAt(characterCount == 0 ? sentenceSize : characterCount + sentenceSize);
                sentencesWithPunctuation.add(sentences.get(i).trim() + punctuation);
                characterCount += sentencesWithPunctuation.get(i).length();
            } else {
                sentencesWithPunctuation.add(sentences.get(i).trim());
            }
        }
        return sentencesWithPunctuation;
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
