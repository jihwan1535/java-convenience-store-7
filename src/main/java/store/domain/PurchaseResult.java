package store.domain;

public record PurchaseResult(
        String name,
        int promotionQuantity,
        int normalQuantity,
        int price
) {

    public static PurchaseResult of(Product product, int promotionQuantity, int normalQuantity) {
        return new PurchaseResult(product.name(), promotionQuantity, normalQuantity, product.price());
    }
}
