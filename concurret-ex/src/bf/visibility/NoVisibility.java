package bf.visibility;

public class NoVisibility {

    private static int num;
    private static boolean ready;

    public static void main(String[] args) {
        // 读线程：可能永远都看不到 ready的值；可能看到了ready，没有看到num，最后输出为0（获取的是失效值）
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!ready) {
                    Thread.yield();
                    System.out.println("num: " + num);
                }
            }
        }).start();

        num = 2;
        ready = true;
    }
}
