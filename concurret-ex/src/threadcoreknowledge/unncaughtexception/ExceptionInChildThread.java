package threadcoreknowledge.unncaughtexception;

/**
 * 子thread 的一场无法用传统方法处理
 */
public class ExceptionInChildThread implements Runnable {
    public static void main(String[] args) {
        //子thread 抛出异常
        new Thread(new ExceptionInChildThread()).start();
        //主线程正常执行
        for (int i = 0; i < 3; i++) {
            System.out.println(i);

        }
    }

    @Override
    public void run() {
        throw new RuntimeException();
    }
}
