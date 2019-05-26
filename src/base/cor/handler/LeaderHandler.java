package base.cor.handler;

/**
 * saleman's leader can permit 15% discount
 * extended class:open extending, close fixing
 */
public class LeaderHandler extends PriceHandler {
    @Override
    public void processDiscount(float discount) {
        if (discount <= 0.15) {
            System.out.format("%s permit discount:%.2f%n",
                    this.getClass().getName(), discount);
        } else {
            //commit the discount thing to his boss to deal with
            successor.processDiscount(discount);
        }
    }
}
