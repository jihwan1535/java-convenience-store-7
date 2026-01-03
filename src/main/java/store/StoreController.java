package store;

import static store.util.ExceptionHandler.handle;

import camp.nextstep.edu.missionutils.DateTimes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import store.application.PurchaseProduct;
import store.domain.Bill;
import store.domain.Product;
import store.domain.ProductRepository;
import store.domain.Promotion;
import store.domain.PromotionRepository;
import store.domain.PurchaseResult;
import store.exception.CustomException;
import store.exception.ExceptionMessage;
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
        boolean running = true;
        while (running) {
            outputView.printIntro();
            List<Product> products = productRepository.getAllProducts();
            outputView.printProducts(products);

            List<PurchaseResult> purchaseResults = new ArrayList<>();
            handle(() -> purchaseProducts(purchaseResults), processError());

            boolean membership = handle(inputView::readMembershipSale, processError());
            Bill bill = Bill.of(purchaseResults, membership);
            outputView.printPurchaseResult(bill);

            running = handle(inputView::readRepurchase, processError());
        }
    }

    private void purchaseProducts(List<PurchaseResult> purchaseResults) {
        List<PurchaseProduct> purchaseProducts = inputView.readPurchaseProduct();
        for (PurchaseProduct purchaseProduct : purchaseProducts) {
            int totalStockQuantity = productRepository.countTotalStock(purchaseProduct.name());
            if (!productRepository.existsProduct(purchaseProduct.name())) {
                throw new CustomException(ExceptionMessage.NOT_FOUND_PRODUCT);
            }
            if (purchaseProduct.quantity() > totalStockQuantity) {
                throw new CustomException(ExceptionMessage.OUT_OF_STOCK);
            }

            int freeQuantity = handle(() -> calculateFreeQuantity(purchaseProduct), processError());
            int purchaseQuantity = purchaseProduct.quantity() + freeQuantity;
            if (!handle(() -> checkCanPromotionSale(purchaseProduct, purchaseQuantity), processError())) {
                continue;
            }
            PurchaseResult purchaseResult = purchase(purchaseProduct, purchaseQuantity);
            purchaseResults.add(purchaseResult);
        }
    }

    private PurchaseResult purchase(PurchaseProduct purchaseProduct, int purchaseQuantity) {
        return productRepository.findPromotionProduct(purchaseProduct.name())
                .map(promotionProduct -> {
                    Promotion promotion = promotionRepository.findPromotion(promotionProduct.promotion());
                    int remainingQuantity = purchaseQuantity - promotionProduct.stock();
                    if (remainingQuantity < 0) {
                        promotionProduct.purchase(purchaseQuantity);
                        int giftQuantity = promotion.countSaleStock(purchaseQuantity, DateTimes.now().toLocalDate());
                        return PurchaseResult.of(promotionProduct, purchaseQuantity, 0, giftQuantity);
                    }
                    int promotionProductStock = promotionProduct.stock();
                    promotionProduct.purchaseAll();
                    Product normalProduct = productRepository.findProduct(purchaseProduct.name());
                    normalProduct.purchase(remainingQuantity);
                    int giftQuantity = promotion.countSaleStock(promotionProductStock, DateTimes.now().toLocalDate());
                    return PurchaseResult.of(normalProduct, promotionProductStock, remainingQuantity, giftQuantity);
                })
                .orElseGet(() -> {
                    Product normalProduct = productRepository.findProduct(purchaseProduct.name());
                    normalProduct.purchase(purchaseQuantity);
                    return PurchaseResult.of(normalProduct, 0, purchaseQuantity, 0);
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
        if (promotion.isInActive(DateTimes.now().toLocalDate())) {
            return true;
        }
        int notAppliedSaleCount = purchaseQuantity - promotionProduct.countPromotionSale(promotion);

        if (notAppliedSaleCount <= 0) {
            return true;
        }
        return inputView.readCanNotSaleCheck(purchaseProduct.name(), notAppliedSaleCount);
    }

    private Consumer<IllegalArgumentException> processError() {
        return (e) -> outputView.printError(e.getMessage());
    }
}
