package store.presentation;

import java.util.List;
import store.domain.Product;

public class OutputView {

    private static void printPromotionProduct(Product product) {
        String format = "- %s %,d원 %s %s%n";
        String stockMessage = convertStockMessage(product.stock());
        System.out.printf(format, product.name(), product.price(), stockMessage, product.promotion());
    }

    private static String convertStockMessage(int stock) {
        if (stock == 0) {
            return "재고 없음";
        }
        return stock + "개";
    }

    private static void printNormalProduct(Product product) {
        String format = "- %s %,d원 %s%n";
        String stockMessage = convertStockMessage(product.stock());
        System.out.printf(format, product.name(), product.price(), stockMessage);
    }

    public void printIntro() {
        System.out.println();
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
    }

    public void printProducts(List<Product> products) {
        System.out.println();
        products.forEach(product -> {
            if (product.isPromotionProduct()) {
                printPromotionProduct(product);
                return;
            }
            printNormalProduct(product);
        });
    }

    public void printError(String message) {
        System.out.println(message);
    }
}
