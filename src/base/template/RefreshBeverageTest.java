package base.template;

public class RefreshBeverageTest {
    public static void main(String[] args) {
        System.out.println("start...");
        RefreshBeverage coffee = new Coffee();
        coffee.prepareBeverageTemplate();
        System.out.println("end");
        System.out.println("-----------");
        RefreshBeverage tea = new Tea();
        tea.prepareBeverageTemplate();
    }
}
