package base.cor.handler;

/**
 * saleman can permit 5% discount
 */
public class CEOHandler extends PriceHandler {
    @Override
    public void processDiscount(float discount) {
        if (discount <= 0.55) {
            System.out.format("%s permit  discount:%.2f%n",
                    this.getClass().getName(), discount);
        } else {
            // no successor for the first-class person
            System.out.format("%s refuse  discount:%.2f%n",
                    this.getClass().getName(), discount);
        }
    }
}
