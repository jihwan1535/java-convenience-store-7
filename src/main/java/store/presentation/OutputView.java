package store.presentation;

import java.util.List;
import store.domain.Bill;
import store.domain.Product;
import store.domain.PurchaseResult;

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

    public void printPurchaseResult(Bill bill) {
        System.out.println("===========W 편의점=============");
        printPurchaseProduct(bill.purchaseHistory());
        if (bill.hasGift()) {
            printGiftProduct(bill.purchaseHistory());
        }
        printPayAmount(bill);
    }

    private void printPayAmount(Bill bill) {
        System.out.println("==============================");
        System.out.printf("총구매액\t\t%d\t%,d%n", bill.calculatePurchaseQuantity(), bill.calculatePurchasePrice());
        System.out.printf("행사할인\t\t\t-%,3d%n", bill.calculatePromotionDiscount());
        System.out.printf("멤버십할인\t\t\t-%,d%n", bill.calculateMembershipDiscount());
        System.out.printf("내실돈\t\t\t %,d%n", bill.calculatePayAmount());
    }

    private void printGiftProduct(List<PurchaseResult> purchaseResults) {
        System.out.println("===========증\t정=============");
        purchaseResults.forEach(result -> {
            if (result.hasGift()) {
                System.out.printf("%s\t\t%d%n", result.name(), result.giftQuantity());
            }
        });
    }

    private void printPurchaseProduct(List<PurchaseResult> purchaseResults) {
        System.out.println("상품명\t\t수량\t금액");
        purchaseResults.forEach(purchaseResult -> System.out.printf("%s\t\t%d \t%,d%n",
                purchaseResult.name(), purchaseResult.totalQuantity(), purchaseResult.price()));
    }
}
