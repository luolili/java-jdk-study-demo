package threadcoreknowledge.unncaughtexception;

public class UseMyHandler implements Runnable {
    public static void main(String[] args) throws InterruptedException {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExHandler("qi1"));
        //try是针对主线程的，无法捕获到子线程的ex,可以在run里面tc
        new Thread(new UseMyHandler(), "t1").start();
        Thread.sleep(100);
        new Thread(new UseMyHandler(), "t2").start();
        Thread.sleep(100);
        new Thread(new UseMyHandler(), "t3").start();
        Thread.sleep(100);
        new Thread(new UseMyHandler(), "t4").start();

    }

    @Override
    public void run() {
        throw new RuntimeException();
    }
}
