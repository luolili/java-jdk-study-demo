package base.cor.handler;

/**
 * generized interface can be an interface or abstract class
 * Chain of Responsibility :cor
 */
public abstract class PriceHandler {

    //self reference
    protected PriceHandler successor;

    public void setSuccessor(PriceHandler successor) {
        this.successor = successor;
    }

    public abstract void processDiscount(float discount);

}
