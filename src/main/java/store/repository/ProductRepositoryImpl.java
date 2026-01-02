package store.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Override
    public Optional<Product> findPromotionProduct(String name) {
        return store.stream()
                .filter(Product::isPromotionProduct)
                .filter(product -> product.equalTo(name))
                .findFirst();
    }

    @Override
    public int countTotalStock(String name) {
        return store.stream()
                .filter(product -> product.equalTo(name))
                .map(Product::stock)
                .reduce(0, Integer::sum);
    }

    @Override
    public Product findProduct(String name) {
        return store.stream()
                .filter(product -> !product.isPromotionProduct())
                .filter(product -> product.equalTo(name))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public int countPromotionProductStock(String name) {
        return findPromotionProduct(name).map(Product::stock).orElseThrow(IllegalStateException::new);
    }
}
