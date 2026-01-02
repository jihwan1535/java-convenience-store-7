package store.domain;

public class Product {

    private static final String NOT_PROMOTION = "null";

    private final String name;
    private final int price;
    private final String promotion;
    private int stock;

    private Product(String name, int price, int stock, String promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
    }

    public static Product from(String... productData) {
        int price = Integer.parseInt(productData[1]);
        int stock = Integer.parseInt(productData[2]);
        return new Product(productData[0], price, stock, productData[3]);
    }

    public static Product createEmptyProduct(Product product) {
        return new Product(product.name, product.price, 0, NOT_PROMOTION);
    }

    public boolean isPromotionProduct() {
        return !promotion.equals(NOT_PROMOTION);
    }

    public boolean equalTo(String otherName) {
        return otherName.equals(this.name);
    }

    public String name() {
        return name;
    }

    public String promotion() {
        return promotion;
    }

    public int price() {
        return price;
    }

    public int stock() {
        return stock;
    }

    public int countPromotionSale(Promotion promotion) {
        return promotion.countSaleStock(stock);
    }
}