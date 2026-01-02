package store.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ExceptionHandler {

    public static <T> T handle(Supplier<T> supplier, Consumer<IllegalArgumentException> exceptionConsumer) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                exceptionConsumer.accept(e);
            }
        }
    }

    public static void handle(Runnable runnable, Consumer<IllegalArgumentException> exceptionConsumer) {
        while (true) {
            try {
                runnable.run();
                return;
            } catch (IllegalArgumentException e) {
                exceptionConsumer.accept(e);
            }
        }
    }
}
