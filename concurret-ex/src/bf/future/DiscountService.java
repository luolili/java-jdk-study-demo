package bf.future;

import java.text.NumberFormat;

public class DiscountService {

    public static void delay() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShop() + " price :" + apply(quote.getPrice(), quote.getDiscount());
    }

    public static String apply(double price, Discount discount) {

        delay();
        return NumberFormat.getInstance().format(price * (100 - discount.percent) / 100);
    }
}
