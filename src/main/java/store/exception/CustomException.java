package store.exception;

public class CustomException extends IllegalArgumentException {

    public CustomException(ExceptionMessage message) {
        super(String.format("[ERROR] %s", message.getMessage()));
    }
}