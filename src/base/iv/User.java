package base.iv;

public class User {
    private String name;
    private boolean isGood;
    private int age;

    public static void main(String[] args) {
        User user = new User();
        System.out.println(user.name);//null
        System.out.println(user.isGood);//false
        System.out.println(user.age);//0


    }
}
