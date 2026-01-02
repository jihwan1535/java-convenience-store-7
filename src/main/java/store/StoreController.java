package store;

import java.util.List;
import store.application.PurchaseProduct;
import store.domain.Product;
import store.presentation.InputMenu;
import store.presentation.InputView;
import store.presentation.OutputView;
import store.util.FileReader;

public class StoreController {

    InputView inputView = new InputView();
    OutputView outputView = new OutputView();

    public void run() {
        while (true) {
            outputView.printIntro();
            String path = "src/main/resources/products.md";
            List<Product> products = FileReader.convertTo(path, Product::from);
            outputView.printProducts(products);

            List<PurchaseProduct> purchaseProducts = inputView.readPurchaseProduct();
            // TODO: 상품 구매 기능
            // TODO: 프로모션 체크
            // TODO: 일반 수량 전환 체크
            // TODO: 멤버십 할인 체크
            InputMenu membershipMenu = inputView.readMembershipSale();

            // TODO: 영수증 출력 체크
            InputMenu repurchaseMenu = inputView.readRepurchase();
            if (repurchaseMenu == InputMenu.N) {
                break;
            }
        }
    }
}
