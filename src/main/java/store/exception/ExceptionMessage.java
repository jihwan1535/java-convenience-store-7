package store.exception;

public enum ExceptionMessage {
    INVALID_INPUT("잘못된 입력입니다."),
    INVALID_PRODUCT_QUANTITY("올바르지 않은 형식으로 입력했습니다."),
    NOT_FOUND_PRODUCT("존재하지 않는 상품입니다."),
    ;
    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}