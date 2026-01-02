package store.domain;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> getAllProducts();

    boolean existsProduct(String name);

    Optional<Product> findPromotionProduct(String name);
}
