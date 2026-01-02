package store;

import java.util.List;
import store.domain.Product;
import store.util.FileReader;

public class StoreController {

    OutputView outputView = new OutputView();

    public void run() {
        outputView.printIntro();
        String path = "src/main/resources/products.md";
        List<Product> products = FileReader.convertTo(path, Product::from);
        outputView.printProducts(products);
    }
}
