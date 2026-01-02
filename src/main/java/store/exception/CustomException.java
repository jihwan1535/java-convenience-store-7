package store.exception;

public class CustomException extends IllegalArgumentException {

    public CustomException(ExceptionMessage message) {
        super(String.format("[ERROR] %s 다시 입력해 주세요.", message.getMessage()));
    }
}