package store.domain;

import java.util.List;

public record Bill(
        List<PurchaseResult> purchaseHistory,
        boolean membership,
        boolean hasGift
) {
    public static Bill of(List<PurchaseResult> purchaseResults, boolean membership) {
        boolean hasGift = purchaseResults.stream().anyMatch(PurchaseResult::hasGift);
        return new Bill(purchaseResults, membership, hasGift);
    }

    public int calculatePurchaseQuantity() {
        return purchaseHistory.stream()
                .map(PurchaseResult::totalQuantity)
                .reduce(0, Integer::sum);
    }

    public int calculatePurchasePrice() {
        return purchaseHistory.stream()
                .map(PurchaseResult::calculateTotalPrice)
                .reduce(0, Integer::sum);
    }

    public int calculatePromotionDiscount() {
        return purchaseHistory.stream()
                .filter(PurchaseResult::hasGift)
                .map(PurchaseResult::calculateGiftPrice)
                .reduce(0, Integer::sum);
    }

    public int calculateMembershipDiscount() {
        if (!membership) {
            return 0;
        }
        int normalProductTotalPrice = purchaseHistory.stream()
                .filter(result -> !result.hasGift())
                .map(PurchaseResult::calculateNormalTotalPrice)
                .reduce(0, Integer::sum);
        return Math.max((int) (normalProductTotalPrice * 0.3), 8000);
    }

    public int calculatePayAmount() {
        return calculatePurchasePrice() - calculatePromotionDiscount() - calculateMembershipDiscount();
    }
}
