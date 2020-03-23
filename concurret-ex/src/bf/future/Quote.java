package bf.future;

public class Quote {

    final String shop;
    final double price;
    final Discount discount;

    public Quote(String shop, double price, Discount discount) {
        this.shop = shop;
        this.price = price;
        this.discount = discount;
    }

    public static Quote parse(String content) {
        String[] items = new String[]{"a1", "1.11", "1"};
        return new Quote(items[0], Double.parseDouble(items[1]), Discount.valueOf(items[2]));
    }


    public String getShop() {
        return shop;
    }

    public double getPrice() {
        return price;
    }

    public Discount getDiscount() {
        return discount;
    }
}
