package base.cor.client;

import base.cor.handler.PriceHandler;
import base.cor.handler.PriceHandlerFactory;

import java.util.Random;

/**
 * the customer has the discount request
 * it depends on the abstract layer,not concrete impls:
 * to reach low coupling
 *
 * spring security
 */
public class Customer {


    //the object(interface) that customer can contact with
    private PriceHandler priceHandler;

    public void setPriceHandler(PriceHandler priceHandler) {
        this.priceHandler = priceHandler;
    }

    //just raise the request without knowing who will process
    //the discount request.the customer just care about the result
    public void requestDiscount(float discount) {
        priceHandler.processDiscount(discount);
    }

    // for our test
    public static void main(String[] args) {

        Customer customer = new Customer();
        customer.setPriceHandler(PriceHandlerFactory.createPriceHandler());

        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(i + " :");

            customer.requestDiscount(r.nextFloat());

        }


    }
}
