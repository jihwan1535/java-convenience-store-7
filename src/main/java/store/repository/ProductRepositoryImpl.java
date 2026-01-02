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
            if (product.isPromotionProduct() && !hasNotNormalProduct(products, product.name())) {
                this.store.add(Product.createEmptyProduct(product));
            }
        });
    }

    private static boolean hasNotNormalProduct(List<Product> products, String productName) {
        return products.stream()
                .filter(product -> !product.isPromotionProduct())
                .anyMatch(product -> product.equalTo(productName));
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(store);
    }

    @Override
    public boolean existsProduct(String name) {
        return store.stream().anyMatch(product -> product.equalTo(name));
    }
}
