package base.template;

/**
 * abstract base class defines the steps of doing the thing
 * 1. refactor: add same code into the parent class
 * 2. re-use
 * 3.shield details
 * drawback:
 * 1.extend
 */
public abstract class RefreshBeverage {

    /**
     * template method for the thing:
     * it can not be changed by subclasses
     */
    public final void prepareBeverageTemplate() {

        //1.
        boilWater();
        //2.
        brew();
        //3.
        pourInCup();
        //4.
        if (isCustomerWantsCondiments()) {

            addCondiments();
        }
    }


    /**
     * not change
     */
    private void boilWater() {
        System.out.println("boil water");
    }

    /**
     * change: delay impl
     */
    abstract void brew();

    /**
     * not change
     */
    private void pourInCup() {
        System.out.println("pour in cup");
    }

    /**
     * change:delay impl
     * problem:you need to add some condiments for every product
     */
    abstract void addCondiments();
    //protected abstract void addCondiments();

    /**
     * hook method:
     * empty or default imp, subclass override it for itself
     *
     * @return
     */
    protected boolean isCustomerWantsCondiments() {
        return true;
    }


}
