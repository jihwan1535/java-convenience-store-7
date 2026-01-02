package store.presentation;

import java.time.DateTimeException;
import java.time.LocalTime;
import store.exception.CustomException;
import store.exception.ExceptionMessage;

public class InputParser {

    public static LocalTime parseTime(String text) {
        String[] parseTime = parseTokens(text, ":");
        try {
            return LocalTime.of(parseInt(parseTime[0]), parseInt(parseTime[1]));
        } catch (DateTimeException e) {
            throw new CustomException(ExceptionMessage.INVALID_INPUT);
        }
    }

    public static int parseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new CustomException(ExceptionMessage.INVALID_INPUT);
        }
    }

    public static String[] parseTokens(String text, String delimiter) {
        try {
            return text.split(delimiter);
        } catch (NumberFormatException e) {
            throw new CustomException(ExceptionMessage.INVALID_INPUT);
        }
    }

    public static String separateBothEnds(String text) {
        return text.substring(1, text.length() - 1);
    }
}
