package base.template;

/**
 * concrete implementation
 */
public class Coffee extends RefreshBeverage {

    @Override
    void brew() {
        System.out.println("brew coffee with boiled water");
    }

    @Override
    void addCondiments() {
        System.out.println("add some sugar for coffee");
    }
}
