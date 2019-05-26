package base.template;

/**
 * concrete implementation
 */
public class Tea extends RefreshBeverage {

    @Override
    void brew() {
        System.out.println("brew tea with boiled water");
    }

    @Override
    void addCondiments() {
        System.out.println("add some lemon for tea");
    }

    /**
     * non=condiment tea
     *
     * @return
     */
    @Override
    protected boolean isCustomerWantsCondiments() {
        return false;
    }
}
