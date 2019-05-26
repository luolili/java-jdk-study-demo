package base.cor.handler;

/**
 * saleman can permit 5% discount
 */
public class ManagerHandler extends PriceHandler {
    @Override
    public void processDiscount(float discount) {
        if (discount <= 0.30) {
            System.out.format("%s permit discount:%.2f%n",
                    this.getClass().getName(), discount);
        } else {
            //commit the discount thing to his boss to deal with
            successor.processDiscount(discount);
        }
    }
}
