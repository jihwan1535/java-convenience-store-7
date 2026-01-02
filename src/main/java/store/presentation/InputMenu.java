package store.presentation;

import java.util.Arrays;
import store.exception.CustomException;
import store.exception.ExceptionMessage;

public enum InputMenu {

    Y, N;

    public static InputMenu of(String menu) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(menu))
                .findFirst()
                .orElseThrow(() -> new CustomException(ExceptionMessage.INVALID_INPUT));
    }
}
