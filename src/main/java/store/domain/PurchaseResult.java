package store.domain;

public record PurchaseResult(
        String name,
        int promotionQuantity,
        int normalQuantity,
        int price,
        int giftQuantity
) {

    public static PurchaseResult of(Product product, int promotionQuantity, int normalQuantity, int giftQuantity) {
        return new PurchaseResult(product.name(), promotionQuantity, normalQuantity, product.price(), giftQuantity);
    }

    public int totalQuantity() {
        return promotionQuantity + normalQuantity;
    }

    public boolean hasGift() {
        return giftQuantity > 0;
    }

    public int calculateTotalPrice() {
        return totalQuantity() * price;
    }

    public int calculateGiftPrice() {
        return giftQuantity * price;
    }

    public int calculateNormalTotalPrice() {
        return normalQuantity * price;
    }
}
