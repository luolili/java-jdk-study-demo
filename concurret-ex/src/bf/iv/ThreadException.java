package bf.iv;

public class ThreadException {
    public static void main(String[] args) throws Throwable {
        //捕获异常
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
                System.out.printf("线程[%s], ex:[%s]", thread.getName(), throwable.getMessage()));
        Thread t = new Thread(() -> {
            throw new RuntimeException("error");
        });
        t.start();
        t.join();
        System.out.println("ii");
        System.out.println(t.isAlive());
    }
}
