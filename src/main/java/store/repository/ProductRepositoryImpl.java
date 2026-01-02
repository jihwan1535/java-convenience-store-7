package store.repository;

import java.util.ArrayList;
import java.util.List;
import store.domain.Product;
import store.domain.ProductRepository;
import store.util.FileReader;

public class ProductRepositoryImpl implements ProductRepository {

    private final List<Product> store;

    public ProductRepositoryImpl() {
        this.store = new ArrayList<>();
        String path = "src/main/resources/products.md";
        List<Product> products = FileReader.convertTo(path, Product::from);
        products.forEach(product -> {
            this.store.add(product);
            if (product.isPromotionProduct() && !hasNotNormalProduct(products, product)) {
                this.store.add(Product.createEmptyProduct(product));
            }
        });
    }

    private static boolean hasNotNormalProduct(List<Product> products, Product compareProduct) {
        return products.stream()
                .filter(product -> !product.isPromotionProduct())
                .anyMatch(product -> product.equalTo(compareProduct));
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(store);
    }
}
