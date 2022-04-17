package com.zonaut.common.stubs;

import org.apache.commons.lang3.RandomUtils;

import java.util.Random;

public final class StubGeneratorUtil {

    private static final Random RANDOM = new Random();

    private StubGeneratorUtil() {
    }

    public static String[] randomWords(int numberOfWords) {
        String[] randomStrings = new String[numberOfWords];
        for (int i = 0; i < numberOfWords; i++) {
            char[] word = new char[RANDOM.nextInt(8) + 3];
            for (int j = 0; j < word.length; j++) {
                word[j] = (char) ('a' + RANDOM.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }

    public static String randomWordsAsString(int numberOfWords) {
        return stringArrayToString(randomWords(numberOfWords), " ");
    }

    public static String stringArrayToString(String[] strArr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr) {
            sb.append(str).append(delimiter);
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static int randomInt() {
        return RandomUtils.nextInt();
    }

    public static boolean randomBoolean() {
        return RandomUtils.nextBoolean();
    }

    public static int randomIntBetween(int startInclusive, int endExclusive) {
        return RandomUtils.nextInt(startInclusive, endExclusive + 1);
    }
}
