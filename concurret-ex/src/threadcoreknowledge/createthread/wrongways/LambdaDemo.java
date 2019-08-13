package threadcoreknowledge.createthread.wrongways;

public class LambdaDemo {
    private String hu;

    public static void main(String[] args) {
        new Thread(() ->
                System.out.println(Thread.currentThread().getName())).start();

        //idea 3个操作
        //-1 在字符串或数字后面.var --定义局部var
        String java = "java";
        int i = 2019;
        //-2 string格式化 .format
        //String.format("hu%s", hu);
        //-3 快速生成lmd :.lamd , Optional : .op
        //() -> System.out.println("--");


    }
}
