package store;

import java.util.List;
import java.util.Optional;
import store.application.PurchaseProduct;
import store.domain.Product;
import store.domain.ProductRepository;
import store.domain.Promotion;
import store.domain.PromotionRepository;
import store.exception.CustomException;
import store.exception.ExceptionMessage;
import store.presentation.InputMenu;
import store.presentation.InputView;
import store.presentation.OutputView;
import store.repository.ProductRepositoryImpl;
import store.repository.PromotionRepositoryImpl;

public class StoreController {

    private final InputView inputView = new InputView();
    private final OutputView outputView = new OutputView();
    private final ProductRepository productRepository = new ProductRepositoryImpl();
    private final PromotionRepository promotionRepository = new PromotionRepositoryImpl();

    public void run() {
        while (true) {
            outputView.printIntro();
            List<Product> products = productRepository.getAllProducts();
            outputView.printProducts(products);

            List<PurchaseProduct> purchaseProducts = inputView.readPurchaseProduct();
            for (PurchaseProduct purchaseProduct : purchaseProducts) {
                if (!productRepository.existsProduct(purchaseProduct.name())) {
                    throw new CustomException(ExceptionMessage.NOT_FOUND_PRODUCT);
                }
                if (!checkCanPromotionSale(purchaseProduct)) {
                    continue;
                }
            }
            // TODO: 상품 구매 기능
            // TODO: 일반 수량 전환 체크
            InputMenu membershipMenu = inputView.readMembershipSale();

            // TODO: 영수증 출력 체크
            InputMenu repurchaseMenu = inputView.readRepurchase();
            if (repurchaseMenu == InputMenu.N) {
                break;
            }
        }
    }

    private boolean checkCanPromotionSale(PurchaseProduct purchaseProduct) {
        Optional<Product> optionalProduct = productRepository.findPromotionProduct(purchaseProduct.name());
        if (optionalProduct.isEmpty()) {
            return true;
        }

        Product promotionProduct = optionalProduct.get();
        Promotion promotion = promotionRepository.findPromotion(promotionProduct.promotion());
        int notAppliedSaleCount = purchaseProduct.quantity() - promotionProduct.countPromotionSale(promotion);

        if (notAppliedSaleCount <= 0) {
            return true;
        }
        return inputView.readCanNotSaleCheck(purchaseProduct.name(), notAppliedSaleCount);
    }
}
