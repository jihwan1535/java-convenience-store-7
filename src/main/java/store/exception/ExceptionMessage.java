package store.exception;

public enum ExceptionMessage {
    INVALID_INPUT("잘못된 입력입니다. 다시 입력해주세요.");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}