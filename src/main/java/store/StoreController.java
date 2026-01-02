package store;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.application.PurchaseProduct;
import store.domain.Product;
import store.domain.ProductRepository;
import store.domain.Promotion;
import store.domain.PromotionRepository;
import store.domain.PurchaseResult;
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

            List<PurchaseResult> purchaseResults = new ArrayList<>();
            List<PurchaseProduct> purchaseProducts = inputView.readPurchaseProduct();
            for (PurchaseProduct purchaseProduct : purchaseProducts) {
                int sum = productRepository.countTotalStock(purchaseProduct.name());
                if (!productRepository.existsProduct(purchaseProduct.name())) {
                    throw new CustomException(ExceptionMessage.NOT_FOUND_PRODUCT);
                }
                if (purchaseProduct.quantity() > sum) {
                    throw new CustomException(ExceptionMessage.OUT_OF_STOCK);
                }

                int freeQuantity = calculateFreeQuantity(purchaseProduct);
                int purchaseQuantity = purchaseProduct.quantity() + freeQuantity;
                if (!checkCanPromotionSale(purchaseProduct, purchaseQuantity)) {
                    continue;
                }
                PurchaseResult purchaseResult = purchase(purchaseProduct, purchaseQuantity);
                purchaseResults.add(purchaseResult);
            }

            InputMenu membershipMenu = inputView.readMembershipSale();

            // TODO: 영수증 출력 체크
            InputMenu repurchaseMenu = inputView.readRepurchase();
            if (repurchaseMenu == InputMenu.N) {
                break;
            }
        }
    }

    private PurchaseResult purchase(PurchaseProduct purchaseProduct, int purchaseQuantity) {
        return productRepository.findPromotionProduct(purchaseProduct.name())
                .map(promotionProduct -> {
                    int remainingQuantity = purchaseProduct.quantity() - promotionProduct.stock();
                    if (remainingQuantity >= 0) {
                        promotionProduct.purchase(purchaseProduct.quantity());
                        return PurchaseResult.of(promotionProduct, purchaseQuantity, 0);
                    }
                    int promotionProductStockQuantity = promotionProduct.stock();
                    promotionProduct.purchaseAll();
                    Product normalProduct = productRepository.findProduct(purchaseProduct.name());
                    normalProduct.purchase(remainingQuantity);
                    return PurchaseResult.of(normalProduct, promotionProductStockQuantity, remainingQuantity);
                })
                .orElseGet(() -> {
                    Product normalProduct = productRepository.findProduct(purchaseProduct.name());
                    normalProduct.purchase(purchaseQuantity);
                    return PurchaseResult.of(normalProduct, 0, purchaseQuantity);
                });
    }

    private int calculateFreeQuantity(PurchaseProduct purchaseProduct) {
        return productRepository.findPromotionProduct(purchaseProduct.name())
                .map(promotionProduct -> {
                    Promotion promotion = promotionRepository.findPromotion(promotionProduct.promotion());
                    if (!promotion.canGetAdditionalQuantity(purchaseProduct.quantity())) {
                        return 0;
                    }
                    int promotionProductStock = productRepository.countPromotionProductStock(purchaseProduct.name());
                    if (promotionProductStock < purchaseProduct.quantity() + promotion.get()) {
                        return 0;
                    }
                    if (inputView.readAdditionalStatus(purchaseProduct.name(), promotion.get())) {
                        return promotion.get();
                    }
                    return 0;
                })
                .orElse(0);
    }

    private boolean checkCanPromotionSale(PurchaseProduct purchaseProduct, int purchaseQuantity) {
        Optional<Product> optionalProduct = productRepository.findPromotionProduct(purchaseProduct.name());
        if (optionalProduct.isEmpty()) {
            return true;
        }

        Product promotionProduct = optionalProduct.get();
        Promotion promotion = promotionRepository.findPromotion(promotionProduct.promotion());
        int notAppliedSaleCount = purchaseQuantity - promotionProduct.countPromotionSale(promotion);

        if (notAppliedSaleCount <= 0) {
            return true;
        }
        return inputView.readCanNotSaleCheck(purchaseProduct.name(), notAppliedSaleCount);
    }
}
