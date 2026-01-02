package store.domain;

import java.time.LocalDate;

public record Promotion(String name, int buy, int get, LocalDate start, LocalDate end) {

    public static Promotion from(String... promotionData) {
        int buy = Integer.parseInt(promotionData[1]);
        int get = Integer.parseInt(promotionData[2]);
        LocalDate start = LocalDate.parse(promotionData[3]);
        LocalDate end = LocalDate.parse(promotionData[3]);
        return new Promotion(promotionData[0], buy, get, start, end);
    }

    public boolean equalTo(String name) {
        return this.name.equals(name);
    }

    public int totalCount() {
        return buy + get;
    }

    public int countSaleStock(int stock) {
        int saleBundle = stock / totalCount();
        return totalCount() * saleBundle;
    }
}
