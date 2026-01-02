package store.presentation;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;
import store.application.PurchaseProduct;
import store.exception.CustomException;
import store.exception.ExceptionMessage;

public class InputView {

    public List<PurchaseProduct> readPurchaseProduct() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String[] tokens = InputParser.parseTokens(Console.readLine(), ",");
        try {
            return Arrays.stream(tokens)
                    .map(token -> {
                        String separated = InputParser.separateBothEnds(token);
                        String[] separatedTokens = InputParser.parseTokens(separated, "-");
                        return new PurchaseProduct(separatedTokens[0], InputParser.parseInt(separatedTokens[1]));
                    })
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new CustomException(ExceptionMessage.INVALID_PRODUCT_QUANTITY);
        }
    }

    public InputMenu readMembershipSale() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return InputMenu.of(Console.readLine());
    }

    public InputMenu readRepurchase() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return InputMenu.of(Console.readLine());
    }

    public boolean readCanNotSaleCheck(String name, int quantity) {
        System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n", name, quantity);
        return InputMenu.of(Console.readLine()) == InputMenu.Y;
    }

    public boolean readAdditionalStatus(String name, int quantity) {
        System.out.printf("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n", name, quantity);
        return InputMenu.of(Console.readLine()) == InputMenu.Y;
    }
}
