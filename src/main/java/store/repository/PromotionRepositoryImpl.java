package store.repository;

import java.util.ArrayList;
import java.util.List;
import store.domain.Promotion;
import store.domain.PromotionRepository;
import store.util.FileReader;

public class PromotionRepositoryImpl implements PromotionRepository {

    private final List<Promotion> promotions;

    public PromotionRepositoryImpl() {
        String path = "src/main/resources/promotions.md";
        List<Promotion> promotions = FileReader.convertTo(path, Promotion::from);
        this.promotions = new ArrayList<>(promotions);
    }

    @Override
    public Promotion findPromotion(String name) {
        return promotions.stream()
                .filter(promotion -> promotion.equalTo(name))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}
