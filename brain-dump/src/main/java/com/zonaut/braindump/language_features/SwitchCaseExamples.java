package com.zonaut.braindump.language_features;

import java.time.DayOfWeek;

public class SwitchCaseExamples {

    public static void main(String[] args) {

        DayOfWeek day = DayOfWeek.SUNDAY;

        switch (day) {
            case MONDAY, FRIDAY, SUNDAY -> System.out.println(6);
            case TUESDAY -> System.out.println(7);
            case THURSDAY, SATURDAY -> System.out.println(8);
            case WEDNESDAY -> System.out.println(9);
        }

        int numLetters = switch (day) {
            case MONDAY, FRIDAY, SUNDAY -> 6;
            case TUESDAY -> 7;
            case THURSDAY, SATURDAY -> 8;
            case WEDNESDAY -> 9;
        };

        // Yielding a value - introduce a new yield
        String j = switch (day) {
            case MONDAY -> "0";
            case TUESDAY -> "1";
            default -> {
                int length = day.toString().length();
                yield getValue(length);
            }
        };

    }

    // Arrow labels
    static String getValue(int length) {
        return switch (length) {
            case 1 -> "A";
            case 2 -> "B";
            default -> "C";
        };
    }

}
