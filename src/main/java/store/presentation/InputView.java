package store.presentation;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;
import store.application.PurchaseProduct;

public class InputView {

    public List<PurchaseProduct> readPurchaseProduct() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        String[] tokens = InputParser.parseTokens(Console.readLine(), ",");
        return Arrays.stream(tokens)
                .map(token -> {
                    String separated = InputParser.separateBothEnds(token);
                    String[] separatedTokens = InputParser.parseTokens(separated, "-");
                    return new PurchaseProduct(separatedTokens[0], InputParser.parseInt(separatedTokens[1]));
                })
                .toList();
    }

    public InputMenu readMembershipSale() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        String input = Console.readLine();
        return InputMenu.of(input);
    }
}
