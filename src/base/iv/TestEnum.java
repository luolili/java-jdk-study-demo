package base.iv;

public class TestEnum {

    static enum Sin {
        GREEN, RED
    }

    static String change(Sin color) {
        if (color == Sin.GREEN) {
            return "Go";
        }
        return null;
    }

    public static void main(String[] args) {
        Sin color = Sin.valueOf("GREEN");

        System.out.println(change(color));//Go
    }
}
